package br.com.nutrido.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.UnidadeMedida;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

// @ApplicationScoped - significa que a classe terah uma instancia criada,
//  que serah compartilhada para quem precisar utilizar OrdemRepository. 
//  A vantagem eh que ha apenas uma alocacao de memoria, pois havera apenas uma instancia compartilhada na aplicacao.
@ApplicationScoped
public class UnidadeMedidaRepository implements PanacheRepository<UnidadeMedida> {

}
