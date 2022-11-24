package br.com.nutrido.model;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Entity(name = "alimento")
public class Alimento extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private Double kcal;

    private Double proteina;

    private Double lipidio;

    private Double carboidrato;

    private Double fibra;

    private Double quantidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "unidade_medida_id")
    private UnidadeMedida unidadeMedida;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "grupo_alimento_id")
    private GrupoAlimento grupoAlimento;

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

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public Double getProteina() {
        return proteina;
    }

    public void setProteina(Double proteina) {
        this.proteina = proteina;
    }

    public Double getLipidio() {
        return lipidio;
    }

    public void setLipidio(Double lipidio) {
        this.lipidio = lipidio;
    }

    public Double getCarboidrato() {
        return carboidrato;
    }

    public void setCarboidrato(Double carboidrato) {
        this.carboidrato = carboidrato;
    }

    public Double getFibra() {
        return fibra;
    }

    public void setFibra(Double fibra) {
        this.fibra = fibra;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public GrupoAlimento getGrupoAlimento() {
        return grupoAlimento;
    }

    public void setGrupoAlimento(GrupoAlimento grupoAlimento) {
        this.grupoAlimento = grupoAlimento;
    }

    public static Uni<Alimento> findByAlimentoId(Long id) {
        return findById(id);
    }

    public static Uni<Alimento> updateAlimento(Long id, Alimento alimento) {
        return Panache
                .withTransaction(() -> findByAlimentoId(id)
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
                        .onFailure().recoverWithNull());
    }

    public static Uni<Alimento> addAlimento(Alimento alimento) {
        return Panache
                .withTransaction(alimento::persist)
                .replaceWith(alimento)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }

    public static Uni<List<PanacheEntityBase>> getAllAlimentos() {
        return Alimento
                .listAll(Sort.by("nome"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));
    }

    public static Uni<Boolean> deleteAlimento(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }

    public static Multi<Alimento> findByGrupoAlimentoId(Long grupoId) {
        return stream("grupoAlimento.id = ?1", grupoId);
    }

    @Override
    public String toString() {
        return "Alimento [id=" + id + ", nome=" + nome + ", kcal=" + kcal + ", proteina=" + proteina + ", lipidio="
                + lipidio + ", carboidrato=" + carboidrato + ", fibra=" + fibra + ", quantidade=" + quantidade
                + ", unidadeMedida=" + unidadeMedida + ", grupoAlimento=" + grupoAlimento + "]";
    }

}
