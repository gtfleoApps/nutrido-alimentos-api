package br.com.nutrido.repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.GrupoAlimento;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class GrupoAlimentoRepository implements PanacheRepository<GrupoAlimento> {

    public Uni<GrupoAlimento> findByGrupoId(Long id) {
        return findById(id);
    }

    public Uni<List<GrupoAlimento>> getAllGroups() {
        return listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<GrupoAlimento>>item(Collections.EMPTY_LIST));
    }

    public Uni<GrupoAlimento> addGrupo(GrupoAlimento grupo) {
        return persist(grupo)
                .replaceWith(grupo)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    public Uni<GrupoAlimento> updateGrupo(Long id, GrupoAlimento grupo) {
        return findByGrupoId(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.setNome(grupo.getNome());
                    return entity;
                })
                .onFailure().recoverWithNull();
    }

    public Uni<Boolean> deleteGrupo(Long id) {
        return deleteById(id);
    }

}
