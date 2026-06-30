package com.greet.repository;

import com.greet.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at"));

        return user;
    };

    @Override
    public User findByUsername(String username) {

        String sql =
                "SELECT * FROM users WHERE username=?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    username
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean save(User user) {

        String sql =
                "INSERT INTO users(username,password,role,email) VALUES(?,?,?,?)";

        int rows = jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getEmail()
        );

        return rows > 0;
    }
}
