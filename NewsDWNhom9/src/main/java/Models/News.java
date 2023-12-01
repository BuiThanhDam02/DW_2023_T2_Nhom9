package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
public class News {
    private int id;
    private String title;
    private String url;
    private String image;
    private String description;
    private String content;
    private String category;
    private String date;

    @Override
    public String toString() {
        return "Models.News{" +
                "id=" + id +
                "\n, title='" + title + '\'' +
                "\n, url='" + url + '\'' +
                "\n, image='" + image + '\'' +
                "\n, description='" + description + '\'' +
                "\n, content='" + content + '\'' +
                "\n, category='" + category + '\'' +
                "\n, date='" + date + '\'' +
                "}\n";
    }
}
