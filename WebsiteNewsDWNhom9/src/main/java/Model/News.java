package Model;


import java.io.Serializable;
import java.sql.Date;

public class News implements Serializable {
    private int id;
    private String title;
    private String image_path;
    private String description;
    private String content;
    private String category_name;
    private Date full_date;
    private String day;
    private String month;
    private String year;

    public News() {
    }

    public News(int id, String title, String image_path, String description, String content, String category_name, Date full_date, String day, String month, String year) {
        this.id = id;
        this.title = title;
        this.image_path = image_path;
        this.description = description;
        this.content = content;
        this.category_name = category_name;
        this.full_date = full_date;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Date getFull_date() {
        return full_date;
    }

    public void setFull_date(Date full_date) {
        this.full_date = full_date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image_path='" + image_path + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", category_name='" + category_name + '\'' +
                ", full_date=" + full_date +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
