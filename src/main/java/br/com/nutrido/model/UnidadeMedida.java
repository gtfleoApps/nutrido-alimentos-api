package br.com.nutrido.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "unidade_medida")
public class UnidadeMedida {

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

    @Override
    public String toString() {
        return "UnidadeMedida [id=" + id + ", nome=" + nome + ", nomeAbreviado=" + nomeAbreviado + "]";
    }

}
