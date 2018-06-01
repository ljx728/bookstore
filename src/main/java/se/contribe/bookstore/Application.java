package se.contribe.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.contribe.bookstore.handler.BookstoreInitiator;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addListeners(new BookstoreInitiator());
		springApplication.run(args);
	}
}
