package co.edu.univalle.estadisticas.dominio;

public record FilaTabla(String equipo, int jugados, int ganados, int empatados, int perdidos,
                        int golesAFavor, int golesEnContra) {

    public static final int PUNTOS_VICTORIA = 3;
    public static final int PUNTOS_EMPATE = 1;

    public static FilaTabla vacia(String equipo) {
        return new FilaTabla(equipo, 0, 0, 0, 0, 0, 0);
    }

    public int diferenciaDeGoles() {
        return golesAFavor - golesEnContra;
    }

    public int puntos() {
        return ganados * PUNTOS_VICTORIA + empatados * PUNTOS_EMPATE;
    }

    FilaTabla sumarPartido(int golesHechos, int golesRecibidos) {
        boolean gano = golesHechos > golesRecibidos;
        boolean empato = golesHechos == golesRecibidos;
        return new FilaTabla(
                equipo,
                jugados + 1,
                ganados + (gano ? 1 : 0),
                empatados + (empato ? 1 : 0),
                perdidos + (!gano && !empato ? 1 : 0),
                golesAFavor + golesHechos,
                golesEnContra + golesRecibidos);
    }
}
