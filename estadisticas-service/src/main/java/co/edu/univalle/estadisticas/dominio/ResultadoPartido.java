package co.edu.univalle.estadisticas.dominio;

public record ResultadoPartido(String equipoLocal, String equipoVisitante, int golesLocal, int golesVisitante) {

    public ResultadoPartido {
        if (equipoLocal == null || equipoLocal.isBlank() || equipoVisitante == null || equipoVisitante.isBlank()) {
            throw new IllegalArgumentException("Los dos equipos son obligatorios");
        }
        if (equipoLocal.equals(equipoVisitante)) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra si mismo");
        }
        if (golesLocal < 0 || golesVisitante < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
    }
}
