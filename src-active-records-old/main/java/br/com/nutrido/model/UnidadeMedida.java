package br.com.nutrido.model;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Entity(name = "unidade_medida")
public class UnidadeMedida extends PanacheEntityBase {

    @Id
    @Column(unique = true)
    private Long id;

    @Column(length = 50, unique = true)
    private String nome;

    @Column(name = "nome_abreviado", length = 5)
    private String nomeAbreviado;

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

    public String getNomeAbreviado() {
        return nomeAbreviado;
    }

    public void setNomeAbreviado(String nomeAbreviado) {
        this.nomeAbreviado = nomeAbreviado;
    }

    public static Uni<UnidadeMedida> findByMedidaId(Long id) {
        return findById(id);
    }

    public static Uni<UnidadeMedida> updateMedida(Long id, UnidadeMedida medida) {
        return Panache
                .withTransaction(() -> findByMedidaId(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.setNome(medida.getNome());
                            entity.setNomeAbreviado(medida.getNomeAbreviado());
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }

    public static Uni<UnidadeMedida> addMedida(UnidadeMedida medida) {
        return Panache
                .withTransaction(medida::persist)
                .replaceWith(medida)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    // public static Uni<List<UnidadeMedida>> getAllMedidas() {
    public static Uni<List<PanacheEntityBase>> getAllMedidas() {
        return UnidadeMedida
                .listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));
    }

    public static Uni<Boolean> deleteMedida(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }

    @Override
    public String toString() {
        return "UnidadeMedida [id=" + id + ", nome=" + nome + ", nomeAbreviado=" + nomeAbreviado + "]";
    }

}
