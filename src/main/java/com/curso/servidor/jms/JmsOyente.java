package com.curso.servidor.jms;

import com.curso.servidor.entidades.Entrada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.curso.servidor.negocio.Negocio;

@Component
public class JmsOyente {

    @Autowired
    private Negocio negocio;

    @JmsListener(destination="${jms.queue.destination}")
    public void miMensaje(String mensajeJson) {
        System.out.println("Recibido:" + mensajeJson);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Entrada entrada =  mapper.readValue(mensajeJson, Entrada.class);
            negocio.grabar(entrada);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            System.out.println("No se pudo registrar");
        }
    }

}
