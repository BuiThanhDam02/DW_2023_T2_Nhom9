INSERT INTO news (title,  image_path,description, content, category_name, full_date, day, month, year)
SELECT :title,  :image_path,:description, :content, :category_name, :full_date, :day, :month, :year
FROM dual
WHERE NOT EXISTS (
        SELECT 1
        FROM news
        WHERE title = :title
                  AND image_path = :image_path
                  AND description = :description
                  AND content = :content
                  AND category_name = :category_name
                  AND full_date = :full_date
                  AND day = :day
                  AND month = :month
                  AND year = :year
    );