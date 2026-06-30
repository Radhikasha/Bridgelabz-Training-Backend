package com.greet.repository;

import com.greet.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GreetingRepositoryImpl implements GreetingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Greeting> rowMapper = (rs, rowNum) -> {

        Greeting g = new Greeting();

        g.setId(rs.getInt("id"));
        g.setMessage(rs.getString("message"));
        g.setImagePath(rs.getString("image_path"));
        g.setCreatedById(rs.getInt("created_by"));
        g.setCreatedByName(rs.getString("username"));
        g.setCreatedAt(rs.getTimestamp("created_at"));

        return g;
    };

    @Override
    public List<Greeting> findAll() {

        String sql =
                """
                SELECT g.*,
                       u.username
                FROM greetings g
                LEFT JOIN users u
                ON g.created_by=u.id
                ORDER BY g.id DESC
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Greeting findById(int id) {

        String sql =
                """
                SELECT g.*,
                       u.username
                FROM greetings g
                LEFT JOIN users u
                ON g.created_by=u.id
                WHERE g.id=?
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    rowMapper,
                    id
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(Greeting greeting) {

        String sql =
                """
                INSERT INTO greetings
                (message,image_path,created_by)
                VALUES(?,?,?)
                """;

        jdbcTemplate.update(
                sql,
                greeting.getMessage(),
                greeting.getImagePath(),
                greeting.getCreatedById()
        );
    }

    @Override
    public void update(Greeting greeting) {

        String sql =
                """
                UPDATE greetings
                SET message=?,
                    image_path=?
                WHERE id=?
                """;

        jdbcTemplate.update(
                sql,
                greeting.getMessage(),
                greeting.getImagePath(),
                greeting.getId()
        );
    }

    @Override
    public void delete(int id) {

        String sql =
                "DELETE FROM greetings WHERE id=?";

        jdbcTemplate.update(sql, id);
    }
}
