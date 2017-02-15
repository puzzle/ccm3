package ch.puzzle.ccm3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ch.puzzle.ccm3.DefaultValues.SWAGGER_BASE_PATH;

@WebServlet("/swagger")
public class SwaggerProvider extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer requestUrl = req.getRequestURL();
        String jsonUrl = requestUrl.substring(0, requestUrl.lastIndexOf(req.getContextPath())) + SWAGGER_BASE_PATH + "/swagger.json";
        String redirectTo = requestUrl + "/index.html?url=" + jsonUrl;
        resp.sendRedirect(redirectTo);
    }
}
