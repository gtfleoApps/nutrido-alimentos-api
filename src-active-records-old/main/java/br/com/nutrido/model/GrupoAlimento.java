package br.com.nutrido.model;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Entity(name = "grupo_alimento")
public class GrupoAlimento extends PanacheEntityBase {

    @Id
    @Column(unique = true)
    private Long id;

    @Column(length = 50, unique = true)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Uni<GrupoAlimento> findByGrupoId(Long id) {
        return findById(id);
    }

    public static Uni<GrupoAlimento> updateGrupo(Long id, GrupoAlimento grupo) {
        return Panache
                .withTransaction(() -> findByGrupoId(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.setNome(grupo.getNome());
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }

    public static Uni<GrupoAlimento> addGrupo(GrupoAlimento grupo) {
        return Panache
                .withTransaction(grupo::persist)
                .replaceWith(grupo)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    // public static Uni<List<GrupoAlimento>> getAllGrupos() {
    public static Uni<List<PanacheEntityBase>> getAllGrupos() {
        return GrupoAlimento
                .listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));

    }

    public static Uni<Boolean> deleteGrupo(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }

    @Override
    public String toString() {
        return "GrupoAlimento [id=" + id + ", nome=" + nome + "]";
    }

}
