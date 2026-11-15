package co.edu.univalle.torneo.evento;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento que torneo-service publica cuando un partido termina.
 * Contrato JSON compartido con estadisticas-service: si se cambia aqui, hay que cambiarlo alla.
 */
public record PartidoFinalizado(
        UUID partidoId,
        String equipoLocal,
        String equipoVisitante,
        int golesLocal,
        int golesVisitante,
        Instant finalizadoEn) {
}
