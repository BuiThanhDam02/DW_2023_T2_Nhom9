INSERT INTO news_staging(title, url, image_url, description, content, category, date, crawler_date, isDelete)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0);

SELECT * FROM news_staging;

TRUNCATE TABLE news_staging;