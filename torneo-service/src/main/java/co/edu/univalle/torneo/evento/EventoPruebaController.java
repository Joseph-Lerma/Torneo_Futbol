package co.edu.univalle.torneo.evento;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint temporal de T-16 (SCRUM-126): publica un PartidoFinalizado con marcador fijo
 * para comprobar que los dos servicios se comunican por RabbitMQ.
 * Se elimina cuando exista el flujo real de registro de resultados.
 */
@RestController
@RequestMapping("/api/pruebas")
public class EventoPruebaController {

    private final PublicadorEventos publicador;

    public EventoPruebaController(PublicadorEventos publicador) {
        this.publicador = publicador;
    }

    @PostMapping("/partido-finalizado")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PartidoFinalizado publicarPartidoDePrueba() {
        PartidoFinalizado evento = new PartidoFinalizado(
                UUID.randomUUID(), "Visca Barça", "Real Univalle", 2, 1, Instant.now());
        publicador.publicar(evento);
        return evento;
    }
}
