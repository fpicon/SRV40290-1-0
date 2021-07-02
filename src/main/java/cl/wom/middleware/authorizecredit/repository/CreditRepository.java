package cl.wom.middleware.authorizecredit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cl.wom.middleware.authorizecredit.model.CreditResponse;

public interface CreditRepository extends MongoRepository<CreditResponse, String> {

}