package com.globalgrupp.courier;

/**
 * Created by Lenovo on 27.01.2016.
 */
import com.vaadin.spring.internal.VaadinSessionScope;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan
@EnableAutoConfiguration
public class App {

    @Bean
    static VaadinSessionScope vaadinSessionScope() {
        return new VaadinSessionScope();
    }

    public static void main(String[] args) {
        //SpringApplication.run(App.class, args);
        new SpringApplicationBuilder().sources(App.class).web(true)
                .logStartupInfo(true).showBanner(true).run(args);

    }

}
