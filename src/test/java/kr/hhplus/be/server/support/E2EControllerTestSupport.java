package kr.hhplus.be.server.support;

import io.restassured.RestAssured;
import kr.hhplus.be.server.support.database.DatabaseCleaner;
import kr.hhplus.be.server.support.database.RedisCacheCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class E2EControllerTestSupport extends IntegrationTestSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private RedisCacheCleaner redisCacheCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clean();
        redisCacheCleaner.clean();
    }
}
