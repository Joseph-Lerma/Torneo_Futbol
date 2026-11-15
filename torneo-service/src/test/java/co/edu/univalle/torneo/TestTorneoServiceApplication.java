package co.edu.univalle.torneo;

import org.springframework.boot.SpringApplication;

public class TestTorneoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TorneoServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
