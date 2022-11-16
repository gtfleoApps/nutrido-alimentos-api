package br.com.nutrido.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.GrupoAlimento;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

@ApplicationScoped
public class GrupoAlimentoRepository implements PanacheRepository<GrupoAlimento> {

}
