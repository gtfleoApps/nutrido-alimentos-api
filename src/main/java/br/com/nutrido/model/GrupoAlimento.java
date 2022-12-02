package br.com.nutrido.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "grupo_alimento")
public class GrupoAlimento {

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

    @Override
    public String toString() {
        return "GrupoAlimento [id=" + id + ", nome=" + nome + "]";
    }

}
