package co.edu.univalle.torneo.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /** Exchange donde torneo-service publica sus eventos de dominio. */
    @Bean
    TopicExchange torneoEventosExchange(@Value("${torneo.eventos.exchange}") String nombre) {
        return new TopicExchange(nombre, true, false);
    }

    /** Los eventos viajan como JSON, no como objetos Java serializados. */
    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
