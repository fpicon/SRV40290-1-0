package cl.wom.middleware.authorizecredit.controller;

import cl.wom.middleware.authorizecredit.model.*;
import cl.wom.middleware.authorizecredit.service.AuthorizeCreditService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;

@RestController
@RequestMapping("${app.service.prefix-url}")
@Validated
public class AuthorizeCreditController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final Map<String, HttpStatus> statusMap = unmodifiableMap(new HashMap<String, HttpStatus>() {{
        put("DONE", HttpStatus.CREATED);
        put("PROCESSING", HttpStatus.ACCEPTED);
        put("ERROR", HttpStatus.BAD_REQUEST);
    }});

    private AuthorizeCreditService authorizeCreditService;

    @Value("${spring.kafka.template.default-topic}")
    private String topicName;
    @Value("${app.config.scoreVerticalValue}")
    private String scoreVerticalValue;
    @Value("${app.config.portabilityVerticalValue}")
    private String portabilityVerticalValue;
    @Value("${app.config.mongoCollectionName}")
    private String mongoCollectionName;

    @Autowired
    public AuthorizeCreditController(AuthorizeCreditService authorizeCreditService) {
        this.authorizeCreditService = authorizeCreditService;
    }

    @PostMapping("/customerInsight/checkCustomerCreditRating")
    ResponseEntity<Response> requestAuthorizeCredit(@Valid @RequestBody @NotNull CustomerCreditRequest body) {
        LOG.info("Start customerInsigh - checkCustomerCreditRating process");

        String vertical = scoreVerticalValue;
        if (body.getPortability() != null && !StringUtils.isEmpty(body.getPortability().getOperator())) {
            vertical = portabilityVerticalValue;
        }

        CreditRequest message = new CreditRequest();
        message.setId(body.getParty().getNationalId() + "-" + vertical);
        message.setData(body);
        val res = authorizeCreditService.sendMessage(topicName, message);
        LOG.info("End customerInsight - checkCustomerCreditRating process");
        if (res.getStatus().equals("DONE")) {
            return new ResponseEntity<>(res.getResponse(), statusMap.get(res.getStatus()));
        }
        return new ResponseEntity<>(res, statusMap.get(res.getStatus()));
    }

    @GetMapping("/customerInsight/checkCustomerCreditRating/{id}")
    ResponseEntity<Response> queryAuthorizeCredit(@PathVariable("id") String id) {
        CreditResponse message = new CreditResponse();
        message.setId(id);
        Optional<CreditResponse> res = authorizeCreditService.getRecordById(id);
        if (!res.isPresent()) {
            ApiError error = new ApiError();
            error.setCode("" + HttpStatus.NOT_FOUND.value());
            error.setMessage("Record not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (res.get().getStatus().equals("DONE")) {
            return new ResponseEntity<>(res.get().getResponse(), statusMap.get(res.get().getStatus()));
        }
        return new ResponseEntity<>(res.get(), statusMap.get(res.get().getStatus()));
    }
}
