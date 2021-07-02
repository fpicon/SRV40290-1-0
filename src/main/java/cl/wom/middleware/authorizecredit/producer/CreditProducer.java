package cl.wom.middleware.authorizecredit.producer;

import org.apache.kafka.common.KafkaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import cl.wom.middleware.authorizecredit.model.CreditRequest;
import cl.wom.middleware.authorizecredit.model.CreditResponse;

@Component
public class CreditProducer {

	/**
	 * Log de la clase..
	 */
	private static Logger log = LogManager.getLogger(CreditProducer.class);
	
	@Value(value = "${spring.kafka.template.default-topic}")
	private String topico;

	@Autowired
	private KafkaTemplate<String, CreditRequest> kafkaTemplate;

	public boolean send(CreditRequest recurrent) {
		log.debug("[send][send] mensaje a inyectar: {} en topico {}", recurrent, topico);
		try {
			ListenableFuture<SendResult<String, CreditRequest>> future = kafkaTemplate.send(topico, recurrent);

			future.addCallback(new ListenableFutureCallback<SendResult<String, CreditRequest>>() {
				@Override
				public void onSuccess(SendResult<String, CreditRequest> result) {
					log.info("[send][CreditProducer] Mensaje inyectado en topico {} OK.", topico);
				}

				@Override
				public void onFailure(Throwable ex) {
					log.error("[send][Throwable][CreditProducer] Error al inyectar el mensaje en topico {}.", topico, ex);
					throw new KafkaException(ex);
				}
			});

			log.info("[send][send] offset mensaje: {}", future.get().getRecordMetadata().offset());
		} catch (Exception e) {
            log.error("[send][CreditProducer] Mensaje no inyectado en topico: {}", topico, e);
			throw new KafkaException(e);
		}
		return true;
	}
}
