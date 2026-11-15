package co.edu.univalle.estadisticas.evento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PartidoFinalizadoListener {

    private static final Logger log = LoggerFactory.getLogger(PartidoFinalizadoListener.class);

    @RabbitListener(queues = "${estadisticas.colas.partido-finalizado}")
    public void alFinalizarPartido(PartidoFinalizado evento) {
        // T-16: por ahora solo se registra en el log. La actualizacion de la tabla llega con HU-01 / Epic 6.
        log.info("Evento PartidoFinalizado recibido: {} {} - {} {} (partido {})",
                evento.equipoLocal(), evento.golesLocal(),
                evento.golesVisitante(), evento.equipoVisitante(),
                evento.partidoId());
    }
}
