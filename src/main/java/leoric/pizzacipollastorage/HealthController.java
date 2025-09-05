package leoric.pizzacipollastorage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;
    @Value("${test.text.string}")
    private String testString;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${security.jwt.secret-key}")
    private String jwtSk;

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("✅ API is up and running ");
    }

    @GetMapping("/db")
    public ResponseEntity<String> healthDb() {
        try (Connection c = dataSource.getConnection()) {
            return ResponseEntity.ok("✅ DB connection OK");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("❌ DB connection FAILED");
        }
    }

//    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> apiJson() {
//        try {
//            ClassPathResource resource = new ClassPathResource("static/Book-network.postman_collection.json");
//            String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//            return ResponseEntity.ok(json);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("{\"error\":\"Unable to read Postman collection\"}");
//        }
//    }
}