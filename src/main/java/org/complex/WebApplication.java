package org.complex;

import java.util.Arrays;

import org.complex.model.Person;
import org.complex.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WebApplication {

	@Autowired
	PersonRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	
	@Component
	public class CommandLineRunnerBean implements CommandLineRunner {	
	    public void run(String... args) {
	    	//clearing database
	    	repository.deleteAll();
	    	//creating some users
	    	repository.saveAll(Arrays.asList(new Person("Adam", "Wątlik"), new Person("Anastazjan", "Wegan"),
					new Person("Apoleoniusz", "Wypiór"), new Person("Amand", "Wymaz"),
					new Person("Awerry", "Wrozny"), new Person("Astygm", "Wyszki")));
	    }
	} 
}
