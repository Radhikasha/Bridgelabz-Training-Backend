package payroll.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import payroll.model.User;
import payroll.repository.UserRepository;
import payroll.util.HashUtil;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userRepository.findByUsername(username);

        if (user == null) {

            request.setAttribute("errorMessage", "Invalid Username");

            request.getRequestDispatcher("login.jsp").forward(request, response);

            return;
        }

        String hashedPassword = HashUtil.hashPassword(password);

        if (!hashedPassword.equals(user.getPassword())) {

            request.setAttribute("errorMessage", "Invalid Password");

            request.getRequestDispatcher("login.jsp").forward(request, response);

            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute("currentUser", user);

        response.sendRedirect("employee?action=list");
    }

}