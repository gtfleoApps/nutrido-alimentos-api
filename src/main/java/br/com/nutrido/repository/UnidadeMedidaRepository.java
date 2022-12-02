package br.com.nutrido.repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.UnidadeMedida;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

// @ApplicationScoped - significa que a classe terah uma instancia criada,
//  que serah compartilhada para quem precisar utilizar OrdemRepository. 
//  A vantagem eh que ha apenas uma alocacao de memoria, pois havera apenas uma instancia compartilhada na aplicacao.
@ApplicationScoped
public class UnidadeMedidaRepository implements PanacheRepository<UnidadeMedida> {

    public Uni<UnidadeMedida> findByMedidaId(Long id) {
        return findById(id);
    }

    public Uni<List<UnidadeMedida>> getAllMedidas() {
        return listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<UnidadeMedida>>item(Collections.EMPTY_LIST));
    }

    public Uni<UnidadeMedida> addMedida(UnidadeMedida medida) {
        return persist(medida)
                .replaceWith(medida)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    public Uni<UnidadeMedida> updateMedida(Long id, UnidadeMedida medida) {
        return findByMedidaId(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.setNome(medida.getNome());
                    entity.setNomeAbreviado(medida.getNomeAbreviado());
                    return entity;
                })
                .onFailure().recoverWithNull();
    }

    public Uni<Boolean> deleteMedida(Long id) {
        return deleteById(id);
    }
}
