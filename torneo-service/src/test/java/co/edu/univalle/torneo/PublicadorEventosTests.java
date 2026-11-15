package co.edu.univalle.torneo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;

import co.edu.univalle.torneo.evento.PartidoFinalizado;
import co.edu.univalle.torneo.evento.PublicadorEventos;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PublicadorEventosTests {

    @Autowired PublicadorEventos publicador;
    @Autowired RabbitTemplate rabbitTemplate;
    @Autowired AmqpAdmin amqpAdmin;
    @Autowired TopicExchange torneoEventosExchange;

    @Test
    void publicaPartidoFinalizadoEnElExchangeDeEventos() {
        // Cola temporal que escucha lo mismo que escuchara estadisticas-service
        Queue cola = new AnonymousQueue();
        amqpAdmin.declareQueue(cola);
        amqpAdmin.declareBinding(BindingBuilder.bind(cola).to(torneoEventosExchange).with("partido.finalizado"));

        PartidoFinalizado enviado = new PartidoFinalizado(
                UUID.randomUUID(), "Visca Barça", "Real Univalle", 2, 1, Instant.parse("2026-11-15T20:00:00Z"));
        publicador.publicar(enviado);

        PartidoFinalizado recibido = rabbitTemplate.receiveAndConvert(
                cola.getName(), 5_000, new ParameterizedTypeReference<PartidoFinalizado>() { });

        assertThat(recibido).isEqualTo(enviado);
    }
}
