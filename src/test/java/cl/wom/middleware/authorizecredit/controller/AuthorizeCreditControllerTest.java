package cl.wom.middleware.authorizecredit.controller;

import cl.wom.middleware.authorizecredit.model.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

public class AuthorizeCreditControllerTest extends AbstractTest {
   @Override
   @Before
   public void setUp() {
      super.setUp();
   }

    @Test
   public void authorizecredit_shouldFailEmptyBody_returnBadRequest() throws Exception {
      String uri = "/customerInsight/checkCustomerCreditRating";
      String inputJson = super.mapToJson(null);
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(inputJson)).andReturn();

      int status = mvcResult.getResponse().getStatus();
      assertEquals(400, status);
      String content = mvcResult.getResponse().getContentAsString();
      ResponseValidation res = super.mapFromJson(content, ResponseValidation.class);
      assertEquals(res.getMessage(), "Empty body or unreadable.");
   }

   @Test
   public void authorizecredit_passValidation_returnOk() throws Exception {
      String uri = "/customerInsight/checkCustomerCreditRating";
      CustomerCreditRequest req = new CustomerCreditRequest();
      ExtraAttribute attrAddData = new ExtraAttribute();
      attrAddData.setName("AUTRENTA");
      attrAddData.setValue("abX5G2GOHM");
      ExtraAttribute attrSourceAddData = new ExtraAttribute();
      attrSourceAddData.setName("AUTRENTA");
      attrSourceAddData.setValue("abX5G2GOHM");
      Party party = new Party();
      party.setCurrency("CLP");
      party.setNationalId("111111111");
      ExtraAttribute attrPartyAddData = new ExtraAttribute();
      attrPartyAddData.setName("IDFIBRA");
      attrPartyAddData.setValue("798044");
      Portability porta = new Portability();
      porta.setAmount("9990");
      porta.setOperator("Virgin Mobile");
      ExtraAttribute attrPortaAddData = new ExtraAttribute();
      attrPortaAddData.setName("REGION");
      attrPortaAddData.setValue("Metropolitana de Santiago");

      req.getAddData().add(attrAddData);
      req.getSourceAddData().add(attrSourceAddData);
      req.getPartyAddData().add(attrPartyAddData);
      req.getPortabilityAddData().add(attrPortaAddData);
      req.setParty(party);
      req.setPortability(porta);

      String inputJson = super.mapToJson(req);
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
         .contentType(MediaType.APPLICATION_JSON_VALUE)
         .content(inputJson)).andReturn();

      int status = mvcResult.getResponse().getStatus();
      assertEquals(202, status);
   }

}
