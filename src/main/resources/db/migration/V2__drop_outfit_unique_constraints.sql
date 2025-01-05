-- Drop unique constraints on outfit items to allow reuse of clothing items
ALTER TABLE outfit 
DROP CONSTRAINT IF EXISTS UKfnmcrad79h5ihgd3pbqtwq1hc,  -- bottom_id constraint
DROP CONSTRAINT IF EXISTS uk_outfit_top_id,             -- top_id constraint
DROP CONSTRAINT IF EXISTS uk_outfit_dress_id,           -- dress_id constraint
DROP CONSTRAINT IF EXISTS uk_outfit_outerwear_id,       -- outerwear_id constraint
DROP CONSTRAINT IF EXISTS uk_outfit_shoes_id;           -- shoes_id constraint 