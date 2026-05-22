-- Supabase SQL Migration: Add columns, enable RLS, create policies

-- 1. Add missing columns to Games
ALTER TABLE public."Games"
  ADD COLUMN IF NOT EXISTS "userId" uuid REFERENCES auth.users(id),
  ADD COLUMN IF NOT EXISTS "isPrivate" boolean NOT NULL DEFAULT false,
  ADD COLUMN IF NOT EXISTS "date" bigint NOT NULL DEFAULT 0;

-- 2. Add missing columns to Players
ALTER TABLE public."Players"
  ADD COLUMN IF NOT EXISTS "userId" uuid REFERENCES auth.users(id),
  ADD COLUMN IF NOT EXISTS "isPrivate" boolean NOT NULL DEFAULT false,
  ADD COLUMN IF NOT EXISTS "deleted" boolean NOT NULL DEFAULT false;

-- 3. Add missing columns to PlayerResults
ALTER TABLE public."PlayerResults"
  ADD COLUMN IF NOT EXISTS "userId" uuid REFERENCES auth.users(id),
  ADD COLUMN IF NOT EXISTS "placement" integer NOT NULL DEFAULT 0,
  ADD COLUMN IF NOT EXISTS "cityCardsPoints" integer,
  ADD COLUMN IF NOT EXISTS "leaderCardsPoints" integer,
  ADD COLUMN IF NOT EXISTS "navalConflictsPoints" integer,
  ADD COLUMN IF NOT EXISTS "islandCardsPoints" integer,
  ADD COLUMN IF NOT EXISTS "navalVictoryPoints" integer;

-- 4. Rename columns to match the new schema (Room names -> Supabase names)
-- Note: These are NO-OP if the columns already have the new names.
-- Run these only if your existing table uses Room column names.
-- PlayerResults columns already created with correct names above; if renaming is needed:
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "gold" TO "moneyPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "blueCardPoints" TO "blueCardsPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "yellowCardPoints" TO "yellowCardsPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "greenCardPoints" TO "greenCardsPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "purpleCardPoints" TO "purpleCardsPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "leaderPoints" TO "leaderCardsPoints";
-- ALTER TABLE public."PlayerResults" RENAME COLUMN "totalScore" TO "totalPoints";

-- 5. Enable Row Level Security
ALTER TABLE public."Games" ENABLE ROW LEVEL SECURITY;
ALTER TABLE public."Players" ENABLE ROW LEVEL SECURITY;
ALTER TABLE public."PlayerResults" ENABLE ROW LEVEL SECURITY;

-- 10. Admin roles table
CREATE TABLE IF NOT EXISTS public."Roles" (
  "userId" uuid PRIMARY KEY REFERENCES auth.users(id),
  "role" text NOT NULL DEFAULT 'user' CHECK ("role" IN ('user', 'admin'))
);

CREATE OR REPLACE FUNCTION public.is_admin()
RETURNS boolean AS $$
  SELECT EXISTS(
    SELECT 1 FROM public."Roles"
    WHERE "userId" = auth.uid() AND "role" = 'admin'
  );
$$ LANGUAGE sql STABLE SECURITY DEFINER;

-- 6. Create policies for Games

-- SELECT: public games visible to all, private games only for owner (admins see all)
DROP POLICY IF EXISTS "Games are viewable by everyone or owner" ON public."Games";
CREATE POLICY "Games are viewable by everyone or owner"
  ON public."Games"
  FOR SELECT
  USING (
    "isPrivate" = false
    OR "userId" = auth.uid()
    OR is_admin()
  );

-- INSERT: only authenticated users can insert their own games
DROP POLICY IF EXISTS "Users can insert their own games" ON public."Games";
CREATE POLICY "Users can insert their own games"
  ON public."Games"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "userId" = auth.uid()
  );

-- UPDATE: only owner can update (admins can update any)
DROP POLICY IF EXISTS "Users can update their own games" ON public."Games";
CREATE POLICY "Users can update their own games"
  ON public."Games"
  FOR UPDATE
  USING ("userId" = auth.uid() OR is_admin())
  WITH CHECK ("userId" = auth.uid() OR is_admin());

-- DELETE: only owner can delete (admins can delete any)
DROP POLICY IF EXISTS "Users can delete their own games" ON public."Games";
CREATE POLICY "Users can delete their own games"
  ON public."Games"
  FOR DELETE
  USING ("userId" = auth.uid() OR is_admin());

-- 7. Create policies for Players

-- SELECT: public + own private players + private players in public games (admins see all)
DROP POLICY IF EXISTS "Players are viewable by everyone or owner or in public games" ON public."Players";
CREATE POLICY "Players are viewable by everyone or owner or in public games"
  ON public."Players"
  FOR SELECT
  USING (
    "isPrivate" = false
    OR "userId" = auth.uid()
    OR "id" IN (
      SELECT "playerID"
      FROM public."PlayerResults" pr
      JOIN public."Games" g ON pr."gameID" = g.id
      WHERE g."isPrivate" = false
    )
    OR is_admin()
  );

-- INSERT: only authenticated users
DROP POLICY IF EXISTS "Users can insert their own players" ON public."Players";
CREATE POLICY "Users can insert their own players"
  ON public."Players"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "userId" = auth.uid()
  );

-- UPDATE: only owner can update (admins can update any)
DROP POLICY IF EXISTS "Users can update their own players" ON public."Players";
CREATE POLICY "Users can update their own players"
  ON public."Players"
  FOR UPDATE
  USING ("userId" = auth.uid() OR is_admin())
  WITH CHECK ("userId" = auth.uid() OR is_admin());

-- DELETE: only owner can delete (admins can delete any)
DROP POLICY IF EXISTS "Users can delete their own players" ON public."Players";
CREATE POLICY "Users can delete their own players"
  ON public."Players"
  FOR DELETE
  USING ("userId" = auth.uid() OR is_admin());

-- 8. Create policies for PlayerResults

-- SELECT: results visible through game visibility (admins see all)
DROP POLICY IF EXISTS "PlayerResults are viewable through game visibility" ON public."PlayerResults";
CREATE POLICY "PlayerResults are viewable through game visibility"
  ON public."PlayerResults"
  FOR SELECT
  USING (
    "gameID" IN (
      SELECT g.id
      FROM public."Games" g
      WHERE g."isPrivate" = false OR g."userId" = auth.uid() OR is_admin()
    )
  );

-- INSERT: only if user owns the game
DROP POLICY IF EXISTS "Users can insert results for their games" ON public."PlayerResults";
CREATE POLICY "Users can insert results for their games"
  ON public."PlayerResults"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid()
    )
  );

-- UPDATE: only if user owns the game (admins can update any)
DROP POLICY IF EXISTS "Users can update results for their games" ON public."PlayerResults";
CREATE POLICY "Users can update results for their games"
  ON public."PlayerResults"
  FOR UPDATE
  USING (
    "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid() OR is_admin()
    )
  );

-- DELETE: only if user owns the game (admins can delete any)
DROP POLICY IF EXISTS "Users can delete results for their games" ON public."PlayerResults";
CREATE POLICY "Users can delete results for their games"
  ON public."PlayerResults"
  FOR DELETE
  USING (
    "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid() OR is_admin()
    )
  );

-- 9. Ensure the auth.uid() is set for new rows via triggers
-- (This is optional - can also set userId in app code when inserting)
CREATE OR REPLACE FUNCTION public.set_user_id()
RETURNS trigger AS $$
BEGIN
  NEW."userId" := auth.uid();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'set_games_user_id') THEN
    CREATE TRIGGER set_games_user_id BEFORE INSERT ON public."Games"
      FOR EACH ROW EXECUTE FUNCTION public.set_user_id();
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'set_players_user_id') THEN
    CREATE TRIGGER set_players_user_id BEFORE INSERT ON public."Players"
      FOR EACH ROW EXECUTE FUNCTION public.set_user_id();
  END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'set_player_results_user_id') THEN
    CREATE TRIGGER set_player_results_user_id BEFORE INSERT ON public."PlayerResults"
      FOR EACH ROW EXECUTE FUNCTION public.set_user_id();
  END IF;
END;
$$;

-- 11. RPC functions for admin-aware mutations (proper error responses instead of silent 0-rows-affected)

CREATE OR REPLACE FUNCTION public.delete_game(game_id BIGINT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM public."Games"
    WHERE id = game_id AND ("userId" = auth.uid() OR is_admin())
  ) THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  DELETE FROM public."PlayerResults" WHERE "gameID" = game_id;
  DELETE FROM public."Games" WHERE id = game_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.update_game_privacy(game_id BIGINT, is_private BOOLEAN)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM public."Games"
    WHERE id = game_id AND ("userId" = auth.uid() OR is_admin())
  ) THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  UPDATE public."Games" SET "isPrivate" = is_private WHERE id = game_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.delete_player(player_id BIGINT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM public."Players"
    WHERE id = player_id AND ("userId" = auth.uid() OR is_admin())
  ) THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  UPDATE public."Players" SET deleted = true WHERE id = player_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.update_player(player_id BIGINT, player_name TEXT, is_private BOOLEAN)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM public."Players"
    WHERE id = player_id AND ("userId" = auth.uid() OR is_admin())
  ) THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  UPDATE public."Players" SET name = player_name, "isPrivate" = is_private WHERE id = player_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.delete_player_results_for_game(game_id BIGINT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM public."Games"
    WHERE id = game_id AND ("userId" = auth.uid() OR is_admin())
  ) THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  DELETE FROM public."PlayerResults" WHERE "gameID" = game_id;
END;
$$;

-- 13. Admin RPCs for deleting user's games and account

CREATE OR REPLACE FUNCTION public.delete_user_games(target_user_id uuid)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT public.is_admin() THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  DELETE FROM public."PlayerResults" WHERE "gameID" IN (
    SELECT id FROM public."Games" WHERE "userId" = target_user_id
  );
  DELETE FROM public."Games" WHERE "userId" = target_user_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.delete_user_account(target_user_id uuid)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT public.is_admin() THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;
  DELETE FROM public."PlayerResults" WHERE "gameID" IN (
    SELECT id FROM public."Games" WHERE "userId" = target_user_id
  );
  DELETE FROM public."Games" WHERE "userId" = target_user_id;
  DELETE FROM public."Players" WHERE "userId" = target_user_id;
  DELETE FROM public."Roles" WHERE "userId" = target_user_id;
  DELETE FROM auth.users WHERE id = target_user_id;
END;
$$;

-- 12. Admin RPCs for user management

CREATE OR REPLACE FUNCTION public.list_users()
RETURNS TABLE(id uuid, email text, display_name text, photo_url text, role text)
LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public
AS $$
  SELECT
    u.id,
    u.email::text,
    u.raw_user_meta_data->>'full_name' AS display_name,
    u.raw_user_meta_data->>'avatar_url' AS photo_url,
    COALESCE(r.role, 'user') AS role
  FROM auth.users u
  LEFT JOIN public."Roles" r ON r."userId" = u.id
  WHERE public.is_admin();
$$;

CREATE OR REPLACE FUNCTION public.set_user_role(target_user_id uuid, new_role text)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER SET search_path = public
AS $$
BEGIN
  IF NOT public.is_admin() THEN
    RAISE EXCEPTION 'You don''t have permission to perform this action';
  END IF;

  IF new_role NOT IN ('user', 'admin') THEN
    RAISE EXCEPTION 'Invalid role. Allowed values: user, admin';
  END IF;

  INSERT INTO public."Roles" ("userId", "role")
  VALUES (target_user_id, new_role)
  ON CONFLICT ("userId")
  DO UPDATE SET "role" = EXCLUDED."role";
END;
$$;
