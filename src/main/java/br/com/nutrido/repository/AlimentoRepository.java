package br.com.nutrido.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.Alimento;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

@ApplicationScoped
public class AlimentoRepository implements PanacheRepository<Alimento> {

    // public Uni<Alimento> persist(AlimentoDTO alimentoDto) {
    // return null;
    // }

}
