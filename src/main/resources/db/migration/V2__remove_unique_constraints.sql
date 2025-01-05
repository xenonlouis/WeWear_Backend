-- Remove unique constraints from outfit table
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS UKdqkt5q8dwhav651jy4dg09efi; -- shoes
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS uk_outfit_top_id; -- top
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS uk_outfit_bottom_id; -- bottom
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS uk_outfit_dress_id; -- dress
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS uk_outfit_outerwear_id; -- outerwear 
ALTER TABLE outfit DROP CONSTRAINT IF EXISTS UKfnmcrad79h5ihgd3pbqtwq1hc; -- outerwear 