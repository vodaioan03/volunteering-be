package org.backend.volunteeringbackend.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
public class DbTestController {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DbTestController(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/db-info")
    public String getDbInfo() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            var meta = conn.getMetaData();
            return String.format(
                    """
                    Database Connection Info:
                    URL: %s
                    Username: %s
                    DB Product: %s %s
                    Driver: %s %s
                    Current Schema: %s
                    Test Query: %s
                    """,
                    meta.getURL(),
                    meta.getUserName(),
                    meta.getDatabaseProductName(),
                    meta.getDatabaseProductVersion(),
                    meta.getDriverName(),
                    meta.getDriverVersion(),
                    jdbcTemplate.queryForObject("SELECT current_schema()", String.class),
                    jdbcTemplate.queryForObject("SELECT 'Connected successfully'", String.class)
            );
        }
    }
}