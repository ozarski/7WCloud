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
  ADD COLUMN IF NOT EXISTS "placement" integer NOT NULL DEFAULT 0;

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

-- 6. Create policies for Games

-- SELECT: public games visible to all, private games only for owner
CREATE POLICY "Games are viewable by everyone or owner"
  ON public."Games"
  FOR SELECT
  USING (
    "isPrivate" = false
    OR "userId" = auth.uid()
  );

-- INSERT: only authenticated users can insert their own games
CREATE POLICY "Users can insert their own games"
  ON public."Games"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "userId" = auth.uid()
  );

-- UPDATE: only owner can update
CREATE POLICY "Users can update their own games"
  ON public."Games"
  FOR UPDATE
  USING ("userId" = auth.uid())
  WITH CHECK ("userId" = auth.uid());

-- DELETE: only owner can delete
CREATE POLICY "Users can delete their own games"
  ON public."Games"
  FOR DELETE
  USING ("userId" = auth.uid());

-- 7. Create policies for Players

-- SELECT: public + own private players + private players in public games
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
  );

-- INSERT: only authenticated users
CREATE POLICY "Users can insert their own players"
  ON public."Players"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "userId" = auth.uid()
  );

-- UPDATE: only owner can update
CREATE POLICY "Users can update their own players"
  ON public."Players"
  FOR UPDATE
  USING ("userId" = auth.uid())
  WITH CHECK ("userId" = auth.uid());

-- DELETE: only owner can delete
CREATE POLICY "Users can delete their own players"
  ON public."Players"
  FOR DELETE
  USING ("userId" = auth.uid());

-- 8. Create policies for PlayerResults

-- SELECT: results visible through game visibility
CREATE POLICY "PlayerResults are viewable through game visibility"
  ON public."PlayerResults"
  FOR SELECT
  USING (
    "gameID" IN (
      SELECT g.id
      FROM public."Games" g
      WHERE g."isPrivate" = false OR g."userId" = auth.uid()
    )
  );

-- INSERT: only if user owns the game
CREATE POLICY "Users can insert results for their games"
  ON public."PlayerResults"
  FOR INSERT
  WITH CHECK (
    auth.role() = 'authenticated'
    AND "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid()
    )
  );

-- UPDATE: only if user owns the game
CREATE POLICY "Users can update results for their games"
  ON public."PlayerResults"
  FOR UPDATE
  USING (
    "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid()
    )
  );

-- DELETE: only if user owns the game
CREATE POLICY "Users can delete results for their games"
  ON public."PlayerResults"
  FOR DELETE
  USING (
    "gameID" IN (
      SELECT g.id FROM public."Games" g WHERE g."userId" = auth.uid()
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
