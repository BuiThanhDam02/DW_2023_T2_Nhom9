package Controller;

import DAO.MartDAO;
import Model.News;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "mainController", value = "/mainController")
public class MainController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        MartDAO martDAO = new MartDAO();
        List<News> newsList = martDAO.get30News();
        List<News> resultList = new ArrayList<>(newsList.subList(1, newsList.size()));
        News news = newsList.get(0);
        request.setAttribute("top1", news);
        request.setAttribute("all", resultList);
        request.getRequestDispatcher("/desktop-8.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
