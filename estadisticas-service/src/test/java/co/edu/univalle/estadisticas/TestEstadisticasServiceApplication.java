package co.edu.univalle.estadisticas;

import org.springframework.boot.SpringApplication;

public class TestEstadisticasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(EstadisticasServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
