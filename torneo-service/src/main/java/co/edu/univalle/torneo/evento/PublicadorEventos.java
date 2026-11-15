package co.edu.univalle.torneo.evento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PublicadorEventos {

    private static final Logger log = LoggerFactory.getLogger(PublicadorEventos.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKeyPartidoFinalizado;

    public PublicadorEventos(RabbitTemplate rabbitTemplate,
                             @Value("${torneo.eventos.exchange}") String exchange,
                             @Value("${torneo.eventos.partido-finalizado.routing-key}") String routingKeyPartidoFinalizado) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKeyPartidoFinalizado = routingKeyPartidoFinalizado;
    }

    public void publicar(PartidoFinalizado evento) {
        rabbitTemplate.convertAndSend(exchange, routingKeyPartidoFinalizado, evento);
        log.info("Evento PartidoFinalizado publicado: {}", evento);
    }
}
