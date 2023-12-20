package Controller;

import DAO.MartDAO;
import Model.News;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "newsDetailController", value = "/newsDetailController")
public class NewsDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println(id);
        MartDAO martDAO = new MartDAO();
        News newsDetail = martDAO.getDetailNews(id);
        newsDetail = ImageService.autowiredImagePath(newsDetail);
        request.setAttribute("newsDetail", newsDetail);
        request.getRequestDispatcher("desktop-9.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
