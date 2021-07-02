package cl.wom.middleware.authorizecredit.controller;

import lombok.val;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.wom.middleware.authorizecredit.model.CreditResponse;
import cl.wom.middleware.authorizecredit.model.Response;

@RestController
public class HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(HealthCheck.class);

    /**
     * Health check.
     *
     * @return OK if the service is running
     */
    @GetMapping(value = "/health")
    public ResponseEntity<Response> getHealthCheck() {

        val response = new CreditResponse();
        response.setStatus("OK");
        LOG.info("Health check ok");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
