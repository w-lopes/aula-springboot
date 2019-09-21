package com.devp.aula.repository;

import org.springframework.data.repository.CrudRepository;
import com.devp.aula.models.Evento;

public interface EventoRepository extends CrudRepository<Evento, String> {
    Evento findByCodigo(long codigo);
}