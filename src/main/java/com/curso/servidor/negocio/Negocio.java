package com.curso.servidor.negocio;

import javax.transaction.Transactional;

import com.curso.servidor.entidades.Entrada;
import com.curso.servidor.repositorio.EntradaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Negocio {

    @Autowired
    private EntradaRepositorio entradaRepositorio;

    @Transactional
    public Entrada grabar(Entrada auto)
    {
        return entradaRepositorio.save(auto);
    }
}
