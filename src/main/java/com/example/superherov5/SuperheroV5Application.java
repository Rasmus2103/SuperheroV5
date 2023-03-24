package com.example.superherov5;

import com.example.superherov5.repositories.ISuperheroRepo;
import com.example.superherov5.repositories.SuperheroRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@SpringBootApplication
public class SuperheroV5Application {

	public static void main(String[] args) {
		SpringApplication.run(SuperheroV5Application.class, args);
	}

}
