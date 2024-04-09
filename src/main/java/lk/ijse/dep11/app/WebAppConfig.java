package lk.ijse.dep11.app;

import lk.ijse.dep11.app.entity.Vehicle;
import lk.ijse.dep11.app.to.VehicleTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebAppConfig {

    @Bean(destroyMethod = "close")
    public EntityManager entityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        return emf.createEntityManager();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(Timestamp.class,String.class).setConverter(ctx -> {
            LocalDateTime localTime = ctx.getSource().toLocalDateTime();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
            return localTime.format(pattern);
        });
        return mapper;
    }
}
