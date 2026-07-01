package payroll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import payroll.model.Employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) -> {

        Employee employee = new Employee();

        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setProfileImage(rs.getString("profile_image"));
        employee.setGender(rs.getString("gender"));

        String dept = rs.getString("departments");

        if (dept != null && !dept.isEmpty()) {
            employee.setDepartments(Arrays.asList(dept.split(",")));
        } else {
            employee.setDepartments(new ArrayList<>());
        }

        employee.setSalary(rs.getBigDecimal("salary"));
        employee.setStartDate(rs.getDate("start_date").toLocalDate());
        employee.setNotes(rs.getString("notes"));
        employee.setCreatedBy(rs.getInt("created_by"));

        return employee;
    };

    public void addEmployee(Employee emp) {

        transactionTemplate.execute(status -> {

            String sql = """
                INSERT INTO employees
                (name, profile_image, gender, salary, start_date, notes, created_by)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

            Integer employeeId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    emp.getName(),
                    emp.getProfileImage(),
                    emp.getGender(),
                    emp.getSalary(),
                    Date.valueOf(emp.getStartDate()),
                    emp.getNotes(),
                    emp.getCreatedBy()
            );

            String deptSql =
                    "INSERT INTO employee_departments(employee_id, department) VALUES (?, ?)";

            if (emp.getDepartments() != null) {
                for (String dept : emp.getDepartments()) {
                    jdbcTemplate.update(
                            deptSql,
                            employeeId,
                            dept
                    );
                }
            }

            return null;
        });

    }

    public List<Employee> findAll() {

        String sql =
                "SELECT e.*, STRING_AGG(d.department, ',') AS departments " +
                        "FROM employees e " +
                        "LEFT JOIN employee_departments d " +
                        "ON e.id=d.employee_id " +
                        "GROUP BY e.id " +
                        "ORDER BY e.id";

        return jdbcTemplate.query(sql, employeeRowMapper);

    }

    public Employee findById(int id) {

        String sql =
                "SELECT e.*, STRING_AGG(d.department, ',') AS departments " +
                        "FROM employees e " +
                        "LEFT JOIN employee_departments d " +
                        "ON e.id=d.employee_id " +
                        "WHERE e.id=? " +
                        "GROUP BY e.id";

        List<Employee> list =
                jdbcTemplate.query(sql, employeeRowMapper, id);

        return list.isEmpty() ? null : list.get(0);

    }

    public Employee findByCreatedBy(int userId) {

        String sql =
                "SELECT " +
                        "e.id, " +
                        "e.name, " +
                        "e.profile_image, " +
                        "e.gender, " +
                        "e.salary, " +
                        "e.start_date, " +
                        "e.notes, " +
                        "e.created_by, " +
                        "STRING_AGG(d.department, ',') AS departments " +
                        "FROM employees e " +
                        "LEFT JOIN employee_departments d " +
                        "ON e.id = d.employee_id " +
                        "WHERE e.created_by = ? " +
                        "GROUP BY " +
                        "e.id, e.name, e.profile_image, e.gender, " +
                        "e.salary, e.start_date, e.notes, e.created_by";

        List<Employee> list =
                jdbcTemplate.query(
                        sql,
                        employeeRowMapper,
                        userId
                );

        return list.isEmpty() ? null : list.get(0);
    }
    public void updateEmployee(int id,
                               BigDecimal salary,
                               String notes,
                               int adminId) {

        String sql =
                "UPDATE employees SET salary=?, notes=? WHERE id=?";

        jdbcTemplate.update(
                sql,
                salary,
                notes,
                id
        );

    }

    public void deleteEmployee(int id) {

        jdbcTemplate.update(
                "DELETE FROM employee_departments WHERE employee_id=?",
                id
        );

        jdbcTemplate.update(
                "DELETE FROM employees WHERE id=?",
                id
        );

    }

    public BigDecimal getDeptPayroll(String dept) {

        String sql =
                "SELECT COALESCE(SUM(e.salary),0) " +
                        "FROM employees e " +
                        "JOIN employee_departments d " +
                        "ON e.id=d.employee_id " +
                        "WHERE d.department=?";

        BigDecimal total = jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class,
                dept
        );

        return total;

    }

    public List<java.util.Map<String, Object>> findAuditLogs() {

        String sql =
                "SELECT * FROM payroll_audit ORDER BY changed_at DESC";

        return jdbcTemplate.queryForList(sql);

    }

}