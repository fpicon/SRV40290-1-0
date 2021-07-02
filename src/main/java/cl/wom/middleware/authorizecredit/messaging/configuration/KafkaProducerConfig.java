package cl.wom.middleware.authorizecredit.messaging.configuration;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import cl.wom.middleware.authorizecredit.model.CreditRequest;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
	
	private final Logger logger = LogManager.getLogger(KafkaProducerConfig.class);

    /**
     * Ruta del sevidor kafka.
     */
    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Protocolo de seguridad para accesar al topico kafka.
     */
    @Value(value = "${spring.kafka.producer.properties.security.protocol}")
    private String securityProtocol;
    
    

    /**
     * Ubicacion de truststore para handshake SSL.
     */
    @Value(value = "${spring.kafka.producer.properties.ssl.truststore.location}")
    private String truststoreLocation;

    /**
     * Password asociada a truststore.
     */
    @Value(value = "${spring.kafka.producer.properties.ssl.truststore.password}")
    private String truststorePass;

    /**
     * Time out para mensajes kafka.
     */
    @Value("${app.kafka.request.timeout}")
    private Integer requestTimeout;
    
    @Value("${app.kafka.block.timeout}")
    private Integer blockTimeout;

    @Value("${app.kafka.ssl.enable}")
    private boolean sslEnable;
       
    
	@Bean
	public KafkaTemplate<String,CreditRequest> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public ProducerFactory<String, CreditRequest> producerFactory() {
		return new DefaultKafkaProducerFactory<>(configProps());
	}
	
    private Map<String, Object> configProps() {
    	logger.info("[configProps] bootstrapAddress: {}", bootstrapAddress);
    	logger.info("[configProps] truststoreLocation: {}", truststoreLocation);
    	logger.info("[configProps] truststorePass: {}", truststorePass);
    	logger.info("[configProps] timeout: {}", blockTimeout);

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, blockTimeout);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
		
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");
		configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		
		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        
		logger.info("SSL is enabled ? {}", sslEnable);
		
        if(sslEnable){
        	 configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
             configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
             configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
                 new String(Base64.getDecoder().decode(truststorePass)));
         }
		
       

        return configProps;
    }
}
