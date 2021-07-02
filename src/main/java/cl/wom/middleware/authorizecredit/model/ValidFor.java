package cl.wom.middleware.authorizecredit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ValidFor {

    private String startDateTime;
    private String endDateTime;

}