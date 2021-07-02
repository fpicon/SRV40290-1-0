package cl.wom.middleware.authorizecredit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Product{

    private String category;
    private Integer creditScore;

}
