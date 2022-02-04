package com.example.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */

@SpringBootApplication
@Theme(value = "flowcrmtutorial")
@PWA(
        name = "Currency Rates",
        shortName = "CR",
        offlinePath = "offline.html",
        offlineResources = {"images/logo.png", "images/offline.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@ComponentScan
@EnableScheduling
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
         //new SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.OFF).run(args);
    }
}
