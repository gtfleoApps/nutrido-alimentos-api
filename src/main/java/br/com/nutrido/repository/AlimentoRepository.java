package br.com.nutrido.repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.nutrido.model.Alimento;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class AlimentoRepository implements PanacheRepository<Alimento> {

    public Uni<Alimento> findByAlimentoId(Long id) {
        return findById(id);
    }

    public Uni<List<Alimento>> getAllAlimentos() {
        return listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<Alimento>>item(Collections.EMPTY_LIST));
    }

    public Uni<List<Alimento>> findByGrupoAlimentoId(Long grupoId) {
        return list("grupo_alimento_id = ?1 ORDER BY nome", grupoId)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<Alimento>>item(Collections.EMPTY_LIST));
    }

    public Uni<Alimento> addAlimento(Alimento alimento) {
        return persist(alimento)
                .replaceWith(alimento)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    public Uni<Alimento> updateAlimento(Long id, Alimento alimento) {
        return findByAlimentoId(id)
                .onItem().ifNotNull()
                .transform(entity -> {
                    entity.setNome(alimento.getNome());
                    entity.setKcal(alimento.getKcal());
                    entity.setProteina(alimento.getProteina());
                    entity.setLipidio(alimento.getLipidio());
                    entity.setCarboidrato(alimento.getCarboidrato());
                    entity.setFibra(alimento.getFibra());
                    entity.setQuantidade(alimento.getQuantidade());
                    entity.setUnidadeMedida(alimento.getUnidadeMedida());
                    entity.setGrupoAlimento(alimento.getGrupoAlimento());
                    return entity;
                })
                .onFailure().recoverWithNull();
    }

    public Uni<Boolean> deleteAlimento(Long id) {
        return deleteById(id);
    }

}
