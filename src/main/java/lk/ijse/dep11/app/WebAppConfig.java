package lk.ijse.dep11.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebAppConfig {

    @Bean
    public EntityManager entityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        return emf.createEntityManager();
    }
}
