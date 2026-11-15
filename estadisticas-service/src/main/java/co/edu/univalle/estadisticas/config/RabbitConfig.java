package co.edu.univalle.estadisticas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /** Se declara tambien aqui para que el servicio arranque aunque torneo-service no haya arrancado. */
    @Bean
    TopicExchange torneoEventosExchange(@Value("${torneo.eventos.exchange}") String nombre) {
        return new TopicExchange(nombre, true, false);
    }

    /** Cola propia de estadisticas-service: los mensajes esperan aqui si el servicio esta caido. */
    @Bean
    Queue colaPartidoFinalizado(@Value("${estadisticas.colas.partido-finalizado}") String nombre) {
        return QueueBuilder.durable(nombre).build();
    }

    @Bean
    Binding bindingPartidoFinalizado(Queue colaPartidoFinalizado, TopicExchange torneoEventosExchange,
                                     @Value("${torneo.eventos.partido-finalizado.routing-key}") String routingKey) {
        return BindingBuilder.bind(colaPartidoFinalizado).to(torneoEventosExchange).with(routingKey);
    }

    /**
     * JSON en lugar de serializacion Java. El tipo destino se deduce del parametro del @RabbitListener,
     * asi que no importa que la clase del evento viva en otro paquete en torneo-service.
     */
    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
