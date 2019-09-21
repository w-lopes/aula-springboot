package com.devp.aula.repository;

import org.springframework.data.repository.CrudRepository;
import com.devp.aula.models.Convidado;
import com.devp.aula.models.Evento;

public interface ConvidadoRepository extends CrudRepository<Convidado, String>{
	Iterable<Convidado> findByEvento(Evento evento);
	Convidado findByRg(String rg);
}