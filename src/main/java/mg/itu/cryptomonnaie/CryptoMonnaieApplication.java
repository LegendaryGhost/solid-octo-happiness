package mg.itu.cryptomonnaie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoMonnaieApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoMonnaieApplication.class, args);
	}
}
