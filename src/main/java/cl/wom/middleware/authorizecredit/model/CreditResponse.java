package cl.wom.middleware.authorizecredit.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "#{@environment.getProperty('app.config.mongoCollectionName')}")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class CreditResponse implements Response{
    private String id;
    private String status;
    private CustomerCreditResponse response;
}
