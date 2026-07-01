package payroll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import payroll.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {

        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));

        return user;
    };

    public User findByUsername(String username) {

        String sql =
                "SELECT * FROM users WHERE username = ?";

        return jdbcTemplate.query(
                sql,
                userRowMapper,
                username
        ).stream().findFirst().orElse(null);
    }

    public void registerUser(String username,
                             String hashedPassword,
                             String email,
                             String role) {

        String sql =
                "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)";

        jdbcTemplate.update(
                sql,
                username,
                hashedPassword,
                email,
                role
        );
    }

}
