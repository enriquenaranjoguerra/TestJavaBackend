package com.opotromatic.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BrowserLauncher {

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        String url = "http://localhost:" + port + contextPath;
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                // Mac
                Runtime.getRuntime().exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux / Unix
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                // Si no detectamos el OS, intentamos el método original como último recurso
                // (Asegúrate de tener spring.main.headless=false si llegas aquí)
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } else {
                    System.out.println("No se pudo abrir el navegador automáticamente. Visita: " + url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}