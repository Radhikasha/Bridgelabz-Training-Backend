package payroll.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import payroll.model.Employee;
import payroll.model.User;
import payroll.repository.EmployeeRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {

    private EmployeeRepository employeeRepository;

    @Override
    public void init() throws ServletException {

        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        employeeRepository = context.getBean(EmployeeRepository.class);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {

            response.sendRedirect("login");
            return;

        }

        User currentUser = (User) session.getAttribute("currentUser");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "add":
                showAddForm(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;

            case "delete":
                deleteEmployee(request, response);
                break;

            case "audit":
                showAuditLogs(request, response);
                break;

            default:
                listEmployees(request, response, currentUser);
                break;

        }

    }
    private void listEmployees(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        if ("ADMIN".equals(currentUser.getRole())) {

            List<Employee> employees = employeeRepository.findAll();

            List<Map<String, Object>> audits = employeeRepository.findAuditLogs();

            request.setAttribute("employees", employees);
            request.setAttribute("audits", audits);

        } else {

            Employee employee =
                    employeeRepository.findByCreatedBy(
                            currentUser.getId());

            request.setAttribute("employee", employee);

        }

        request.setAttribute("currentUser", currentUser);

        request.getRequestDispatcher("list-employees.jsp").forward(request, response);

    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("add-employee.jsp").forward(request, response);

    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Employee employee = employeeRepository.findById(id);

        request.setAttribute("employee", employee);

        request.getRequestDispatcher("edit-employee.jsp").forward(request, response);

    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        employeeRepository.deleteEmployee(id);

        response.sendRedirect("employee?action=list");

    }

    private void showAuditLogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> audits = employeeRepository.findAuditLogs();

        request.setAttribute("audits", audits);

        request.getRequestDispatcher("list-employees.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {

            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {

            createEmployee(request, response);

        } else if ("update".equals(action)) {

            updateEmployee(request, response);

        } else {

            response.sendRedirect("employee?action=list");

        }

    }

    private void createEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        User currentUser = (User) session.getAttribute("currentUser");

        Employee employee = new Employee();

        employee.setName(request.getParameter("name"));

        employee.setProfileImage(request.getParameter("profileImage"));

        employee.setGender(request.getParameter("gender"));

        String[] departments = request.getParameterValues("departments");

        if (departments != null) {

            employee.setDepartments(Arrays.asList(departments));

        }

        employee.setSalary(new BigDecimal(request.getParameter("salary")));

        employee.setStartDate(LocalDate.parse(request.getParameter("startDate")));

        employee.setNotes(request.getParameter("notes"));

        employee.setCreatedBy(currentUser.getId());

        employeeRepository.addEmployee(employee);

        response.sendRedirect("employee?action=list");

    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        User currentUser = (User) session.getAttribute("currentUser");

        int id = Integer.parseInt(request.getParameter("id"));

        BigDecimal salary = new BigDecimal(request.getParameter("salary"));

        String notes = request.getParameter("notes");

        employeeRepository.updateEmployee(
                id,
                salary,
                notes,
                currentUser.getId());

        response.sendRedirect("employee?action=list");

    }

}