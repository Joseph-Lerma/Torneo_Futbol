package co.edu.univalle.estadisticas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class PartidoFinalizadoListenerTests {

    @Autowired RabbitTemplate rabbitTemplate;

    @Test
    void recibeElEventoTalComoLoPublicaTorneoService(CapturedOutput salida) {
        // Mismo JSON y mismo __TypeId__ (clase de OTRO paquete) que pone torneo-service al publicar
        String json = """
                {"partidoId":"3f1c2a9e-0000-4000-8000-000000000001","equipoLocal":"Visca Barça",
                 "equipoVisitante":"Real Univalle","golesLocal":2,"golesVisitante":1,
                 "finalizadoEn":"2026-11-15T20:00:00Z"}""";
        Message mensaje = MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader("__TypeId__", "co.edu.univalle.torneo.evento.PartidoFinalizado")
                .build();

        rabbitTemplate.send("torneo.eventos", "partido.finalizado", mensaje);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(salida.getOut()).contains(
                        "Evento PartidoFinalizado recibido: Visca Barça 2 - 1 Real Univalle"));
    }
}
