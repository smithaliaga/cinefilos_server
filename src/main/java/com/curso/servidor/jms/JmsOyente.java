package com.curso.servidor.jms;

import com.curso.servidor.entidades.Entrada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.curso.servidor.negocio.Negocio;

@Component
public class JmsOyente {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Negocio negocio;

    @Value("${jms.cola.respuesta}")
    String destinationQueue;

    @JmsListener(destination="${jms.cola.envio}")
    public void miMensaje(String mensajeJson, JmsMessageHeaderAccessor headerAccessor) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String corrId = headerAccessor.getCorrelationId();
            System.out.println("RECIBE - CORRELATION ID: " + corrId + " , CUERPO: " + mensajeJson);

            Entrada entrada =  mapper.readValue(mensajeJson, Entrada.class);
            negocio.grabar(entrada);

            String jsonString = mapper.writeValueAsString(entrada);

            System.out.println("RESPONDE - CORRELATION ID: " + corrId + " , CUERPO: " + jsonString);

            jmsTemplate.convertAndSend(destinationQueue, jsonString, m -> {
                m.setJMSCorrelationID(corrId);
                return m;
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            System.out.println("No se pudo registrar");
        }
    }
}
