package com.example.ex4;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex4Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex4Application.class, args);
		System.out.println("http://localhost:8080/ex4");
	}

}


