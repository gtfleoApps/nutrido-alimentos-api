package br.com.nutrido.dto;

import io.smallrye.common.constraint.NotNull;

public class AlimentoDTO {

    // private Long id;

    @NotNull
    private String nome;

    @NotNull
    private Double kcal;

    @NotNull
    private Double proteina;

    @NotNull
    private Double lipidio;

    @NotNull
    private Double carboidrato;

    @NotNull
    private Double fibra;

    @NotNull
    private Double quantidade;

    @NotNull
    private Long unidadeMedidaId;

    @NotNull
    private Long grupoAlimentoId;

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

    public Long getUnidadeMedidaId() {
        return unidadeMedidaId;
    }

    public void setUnidadeMedidaId(Long unidadeMedidaId) {
        this.unidadeMedidaId = unidadeMedidaId;
    }

    public Long getGrupoAlimentoId() {
        return grupoAlimentoId;
    }

    public void setGrupoAlimentoId(Long grupoAlimentoId) {
        this.grupoAlimentoId = grupoAlimentoId;
    }

}
