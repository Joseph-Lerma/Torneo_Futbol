package co.edu.univalle.estadisticas.evento;

import java.time.Instant;
import java.util.UUID;

/**
 * Copia local del evento que publica torneo-service.
 * Contrato JSON compartido: si se cambia en torneo-service, hay que cambiarlo aqui.
 */
public record PartidoFinalizado(
        UUID partidoId,
        String equipoLocal,
        String equipoVisitante,
        int golesLocal,
        int golesVisitante,
        Instant finalizadoEn) {
}
