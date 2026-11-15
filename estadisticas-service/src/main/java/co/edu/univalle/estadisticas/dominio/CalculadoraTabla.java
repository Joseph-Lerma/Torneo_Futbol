package co.edu.univalle.estadisticas.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Calcula la tabla de posiciones con las reglas del torneo:
 * victoria 3 puntos, empate 1, y desempate por diferencia de goles y luego por goles a favor.
 * Si dos equipos siguen empatados se ordenan por nombre para que el resultado sea siempre el mismo.
 */
public class CalculadoraTabla {

    private static final Comparator<FilaTabla> ORDEN_TABLA = Comparator
            .comparingInt(FilaTabla::puntos).reversed()
            .thenComparing(Comparator.comparingInt(FilaTabla::diferenciaDeGoles).reversed())
            .thenComparing(Comparator.comparingInt(FilaTabla::golesAFavor).reversed())
            .thenComparing(FilaTabla::equipo);

    public List<FilaTabla> calcular(List<ResultadoPartido> partidos) {
        Map<String, FilaTabla> filas = new LinkedHashMap<>();
        for (ResultadoPartido p : partidos) {
            sumar(filas, p.equipoLocal(), p.golesLocal(), p.golesVisitante());
            sumar(filas, p.equipoVisitante(), p.golesVisitante(), p.golesLocal());
        }
        List<FilaTabla> tabla = new ArrayList<>(filas.values());
        tabla.sort(ORDEN_TABLA);
        return tabla;
    }

    private void sumar(Map<String, FilaTabla> filas, String equipo, int golesHechos, int golesRecibidos) {
        FilaTabla actual = filas.getOrDefault(equipo, FilaTabla.vacia(equipo));
        filas.put(equipo, actual.sumarPartido(golesHechos, golesRecibidos));
    }
}
