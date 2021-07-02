package cl.wom.middleware.authorizecredit.service;

import cl.wom.middleware.authorizecredit.model.*;
import cl.wom.middleware.authorizecredit.producer.CreditProducer;
import cl.wom.middleware.authorizecredit.repository.CreditRepository;
import cl.wom.middleware.authorizecredit.constants.WSAuthorizeCreditConstants;
import lombok.val;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeCreditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizeCreditService.class);

    @Autowired
    private CreditProducer creditProducer;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public CreditResponse sendMessage(String topicName, CreditRequest message){
        val response = new CreditResponse();
        response.setId(message.getId());
        try{
            LOG.info("Checking if the record with key: {} already exist in mongodb", message.getId());
            
            Optional<CreditResponse> exist = getRecordById(message.getId());
            
            if( exist.isPresent()){
                
            	LOG.info("Message with key {} already exist in mongodb, message: {}", exist.get().getId(), exist.toString());
                
            	if(exist.get().getStatus().equalsIgnoreCase(WSAuthorizeCreditConstants.ERROR_STATE_DOC) || Objects.isNull(exist.get().getResponse())){
                    LOG.info("Message with key {} exist on mongo with status ERROR or Response is null, sending the message again to retry.", exist.get().getId());
                }else{
                    if(!Objects.isNull(exist.get().getResponse())){
                        response.setResponse(exist.get().getResponse());
                    }
                    response.setStatus(exist.get().getStatus());
                    return response;
                }
            }else{
                LOG.info("Record with key: {} do not exist on mongo. Continue...", message.getId());
            }

            LOG.info("Message already sent to the topic, save with state PROCESING in mongodb, message: {}", message.toString());
            response.setStatus(WSAuthorizeCreditConstants.PROCESING_STATE_DOC);
            creditRepository.save(response);
            LOG.info("Message updated successfully in mongodb with state {}, message: {}", response.getStatus(), message.toString());
            
            
            LOG.info("Message ready to send to message: {}", message.toString());
            creditProducer.send(message);
            LOG.info("Message sent.");
            
        }catch(Exception e){
            response.setStatus(WSAuthorizeCreditConstants.ERROR_STATE_DOC);
            LOG.error("Error sending the message e, detail: {}", e.getMessage());
        }
        return response;
    }

    public Optional<CreditResponse> getRecordById(String id){
        return creditRepository.findById(id);
    }
}
