package co.edu.univalle.estadisticas.dominio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

class CalculadoraTablaTest {

    private final CalculadoraTabla calculadora = new CalculadoraTabla();

    private FilaTabla filaDe(List<FilaTabla> tabla, String equipo) {
        return tabla.stream().filter(f -> f.equipo().equals(equipo)).findFirst().orElseThrow();
    }

    private List<String> orden(List<FilaTabla> tabla) {
        return tabla.stream().map(FilaTabla::equipo).toList();
    }

    @Test
    void victoriaDaTresPuntos() {
        List<FilaTabla> tabla = calculadora.calcular(List.of(new ResultadoPartido("A", "B", 2, 0)));

        assertThat(filaDe(tabla, "A").puntos()).isEqualTo(3);
        assertThat(filaDe(tabla, "B").puntos()).isZero();
        assertThat(filaDe(tabla, "A").ganados()).isEqualTo(1);
        assertThat(filaDe(tabla, "B").perdidos()).isEqualTo(1);
    }

    @Test
    void empateDaUnPuntoACadaEquipo() {
        List<FilaTabla> tabla = calculadora.calcular(List.of(new ResultadoPartido("A", "B", 1, 1)));

        assertThat(filaDe(tabla, "A").puntos()).isEqualTo(1);
        assertThat(filaDe(tabla, "B").puntos()).isEqualTo(1);
        assertThat(filaDe(tabla, "A").empatados()).isEqualTo(1);
    }

    @Test
    void ordenaPrimeroPorPuntosAunqueOtroTengaMejorDiferencia() {
        // B: 6 pts y DG +2; A: 3 pts y DG +4; C: 0 pts
        List<FilaTabla> tabla = calculadora.calcular(List.of(
                new ResultadoPartido("B", "A", 1, 0),
                new ResultadoPartido("B", "C", 1, 0),
                new ResultadoPartido("A", "C", 5, 0)));

        assertThat(orden(tabla)).containsExactly("B", "A", "C");
    }

    @Test
    void desempataPorDiferenciaDeGoles() {
        // A y B quedan con 3 puntos; A tiene DG +3 y B +1
        List<FilaTabla> tabla = calculadora.calcular(List.of(
                new ResultadoPartido("B", "D", 1, 0),
                new ResultadoPartido("A", "C", 3, 0)));

        assertThat(filaDe(tabla, "A").puntos()).isEqualTo(filaDe(tabla, "B").puntos());
        assertThat(orden(tabla).subList(0, 2)).containsExactly("A", "B");
    }

    @Test
    void desempataPorGolesAFavorCuandoLaDiferenciaEsIgual() {
        // A y B quedan con 3 puntos y DG +1; B hizo 3 goles y A solo 1
        List<FilaTabla> tabla = calculadora.calcular(List.of(
                new ResultadoPartido("A", "C", 1, 0),
                new ResultadoPartido("B", "D", 3, 2)));

        assertThat(filaDe(tabla, "A").diferenciaDeGoles()).isEqualTo(filaDe(tabla, "B").diferenciaDeGoles());
        assertThat(orden(tabla).subList(0, 2)).containsExactly("B", "A");
    }

    @Test
    void calculaGolesYPartidosJugados() {
        List<FilaTabla> tabla = calculadora.calcular(List.of(
                new ResultadoPartido("A", "B", 2, 1),
                new ResultadoPartido("B", "A", 3, 3)));

        FilaTabla a = filaDe(tabla, "A");
        assertThat(a.jugados()).isEqualTo(2);
        assertThat(a.golesAFavor()).isEqualTo(5);
        assertThat(a.golesEnContra()).isEqualTo(4);
        assertThat(a.diferenciaDeGoles()).isEqualTo(1);
        assertThat(a.puntos()).isEqualTo(4);
    }

    @Test
    void sinPartidosLaTablaEstaVacia() {
        assertThat(calculadora.calcular(List.of())).isEmpty();
    }

    @Test
    void rechazaGolesNegativos() {
        assertThatThrownBy(() -> new ResultadoPartido("A", "B", -1, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
