package DAO;

import DatabaseConfig.JDBIConnector;
import Model.Config;
import Model.ConfigFile;
import Model.News;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.stream.Collectors;

public class MartDAO {

    public List<News> get30News() {
        ControlDAO controlDAO = new ControlDAO();
        Config c = controlDAO.getCurrentConfig();
        Jdbi mart_jdbi = new JDBIConnector().get(c.getMartSourceHost(),c.getMartSourcePort(),c.getMartDbName(),c.getMartSourceUsername(),c.getMartSourcePassword());

        List<News> newsList = mart_jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM news ORDER BY id DESC LIMIT 30")
                    .mapToBean(News.class).stream().collect(Collectors.toList());
        });
        return newsList;
    }
    public News getDetailNews(int id) {
        ControlDAO controlDAO = new ControlDAO();
        Config c = controlDAO.getCurrentConfig();
        Jdbi mart_jdbi = new JDBIConnector().get(c.getMartSourceHost(),c.getMartSourcePort(),c.getMartDbName(),c.getMartSourceUsername(),c.getMartSourcePassword());
        return mart_jdbi.withHandle(handle ->
            handle.createQuery("SELECT *\n" +
                    "FROM news\n" +
                    "WHERE id = ?")
                    .bind(0, id)
                    .mapToBean(News.class)
                    .first()
        );
    }

    public static void main(String[] args) {
        System.out.println(new MartDAO().getDetailNews(1));
    }
}
