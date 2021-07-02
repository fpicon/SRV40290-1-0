package cl.wom.middleware.authorizecredit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthorizeCredit{

    @JsonProperty("@baseType")
    private String baseType;
    @JsonProperty("@schemaLocation")
    private String schemaLocation;
    @JsonProperty("@type")
    private String type;
    private String creditProfileDate;
    private Integer creditRiskRating;
    private String idProfile;
    private String attentionLevel;

}
