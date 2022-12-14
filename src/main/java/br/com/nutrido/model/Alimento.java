package br.com.nutrido.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.smallrye.common.constraint.NotNull;

@Entity(name = "alimento")
public class Alimento {

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

    /*
     * If you look at it carefully, you will find that if the relationship ends with
     * Many keyword i.e. OneToMany , ManyToMany , it is Lazy. If it ends with One
     * i.e. ManyToOne , OneToOne , it is Eager.
     */
    // private Long unidadeMedidaId;
    // @JsonBackReference
    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "unidade_medida_id")
    private UnidadeMedida unidadeMedida;

    // private Long grupoAlimentoId;
    // @JsonBackReference
    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    @Override
    public String toString() {
        return "Alimento [id=" + id + ", nome=" + nome + ", kcal=" + kcal + ", proteina=" + proteina + ", lipidio="
                + lipidio + ", carboidrato=" + carboidrato + ", fibra=" + fibra + ", quantidade=" + quantidade
                + ", unidadeMedida=" + unidadeMedida + ", grupoAlimento=" + grupoAlimento + "]";
    }

}
