package payroll.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import payroll.repository.UserRepository;
import payroll.util.HashUtil;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {

        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        userRepository = context.getBean(UserRepository.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("register.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = "USER";

        UserRepository repo = userRepository;

        if (repo.findByUsername(username) != null) {

            request.setAttribute("errorMessage", "Username already exists.");

            request.getRequestDispatcher("register.jsp").forward(request, response);

            return;
        }

        String hashedPassword = HashUtil.hashPassword(password);

        repo.registerUser(username, hashedPassword, email, role);

        response.sendRedirect("login");

    }

}