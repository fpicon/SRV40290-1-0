package cl.wom.middleware.authorizecredit.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class CustomerCreditRequest {

    @NotNull
    @Valid
    private Source source;

    @Valid
    private List<ExtraAttribute> sourceAddData = new ArrayList<>();

    @NotNull(message = "Missing party")
    @Valid
    private Party party;

    @Valid
    private List<ExtraAttribute> partyAddData = new ArrayList<>();

    @Valid
    private Portability portability;

    @Valid
    private List<ExtraAttribute> portabilityAddData = new ArrayList<>();

    @Valid
    private List<ExtraAttribute> addData = new ArrayList<>();

    @Data
    public static class Source {
        @NotNull
        private String idApp;
        @NotNull
        private String user;
        @NotNull
        private String channel;
        @NotNull
        private String branch;
    }
}
