package cl.wom.middleware.authorizecredit.controller;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RequestMapping("/docs")
@RestController
public class SwaggerController {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String swaggerJson = null;

    @GetMapping("/swagger.json")
    @SneakyThrows
    public ResponseEntity<?> getSwagger() {
        if (swaggerJson != null) {
            return new ResponseEntity<>(swaggerJson, HttpStatus.OK);
        }
        val resource = resourceLoader.getResource("classpath:swagger.json");
        assert resource.exists();

        @Cleanup val swaggerStream = resource.getInputStream();
        swaggerJson = new BufferedReader(new InputStreamReader(swaggerStream))
                .lines().collect(Collectors.joining("\n"));

        val headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(swaggerJson, headers, HttpStatus.OK);
    }
}
