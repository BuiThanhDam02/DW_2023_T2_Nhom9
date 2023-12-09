-- # insert_return_id_category
-- ## start
INSERT INTO category_dim (category_name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM category_dim WHERE category_name = ?);
SELECT id FROM category_dim WHERE category_name = ?;
-- ## end

-- # insert_return_id_image_url
-- ## start
INSERT INTO image_url_dim (image_path) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM image_url_dim WHERE image_path = ?);
SELECT id FROM image_url_dim WHERE image_path = ?;
-- ## end

-- # insert_return_id_date
-- ## start
INSERT INTO date_dim (full_date, day, month, year, GMT) SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM date_dim WHERE full_date = ?);
SELECT id FROM date_dim WHERE full_date = ?;
-- ## end

-- # insert_return_id_date_expired
-- ## start
INSERT INTO date_expired_dim (full_date, day, month, year, GMT) SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM date_expired_dim WHERE full_date = ?);
SELECT id FROM date_expired_dim WHERE full_date = ?;
-- ## end