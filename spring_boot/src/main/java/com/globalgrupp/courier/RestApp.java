package com.globalgrupp.courier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by п on 01.02.2016.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class RestApp {


        public static void main(String[] args) {
            SpringApplication.run(App.class, args);
        }

    }
