package com.exemplo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Habilita tarefas agendadas
public class ParabensWhatsappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParabensWhatsappApplication.class, args);
    }
}

