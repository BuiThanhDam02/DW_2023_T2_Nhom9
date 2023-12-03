CREATE PROCEDURE aggregate_procedure()
BEGIN
	TRUNCATE TABLE `aggregate`;
	INSERT INTO `aggregate`(fact_id, title, description,content,isdelete,image_path,category_name,full_date,day,month,year,GMT)
	SELECT news_warehouse.id,news_warehouse.title,news_warehouse.description,news_warehouse.content,news_warehouse.isdelete,
			image_url_dim.image_path,category_dim.category_name,date_dim.full_date,date_dim.`day`,date_dim.`month`,date_dim.`month`,			date_dim.GMT
	FROM news_warehouse 
			JOIN image_url_dim ON news_warehouse.image_url = image_url_dim.id
			JOIN category_dim ON news_warehouse.category = category_dim.id
			JOIN date_dim ON news_warehouse.date = date_dim.id;
END

