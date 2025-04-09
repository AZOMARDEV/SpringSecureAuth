package com.mistervoiture.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
public class MvauthApplication {

	@GetMapping("/")
	public String getMessage(){
		return "Accessed By https protocol";
	}

	public static void main(String[] args) {
		SpringApplication.run(MvauthApplication.class, args);
	}

	@Bean
	public TomcatServletWebServerFactory servletWebServerFactory(){
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addConnectorCustomizers(connect -> {
			connect.setRedirectPort(2030);
		});
		return tomcat;
	}
}