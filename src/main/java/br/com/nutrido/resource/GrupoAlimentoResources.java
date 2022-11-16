package br.com.nutrido.resource;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import br.com.nutrido.model.GrupoAlimento;
import br.com.nutrido.repository.GrupoAlimentoRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Path("/grupos")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class GrupoAlimentoResources {

    @Inject
    GrupoAlimentoRepository grupoAlimentoRepository;

    @GET
    public Uni<List<GrupoAlimento>> get() {
        return grupoAlimentoRepository.listAll(Sort.by("nome"));
    }

    @GET
    @Path("{id}")
    public Uni<GrupoAlimento> getSingle(Long id) {
        return grupoAlimentoRepository.findById(id);
    }

    @POST
    @ReactiveTransactional
    public Uni<GrupoAlimento> create(GrupoAlimento grupo) {
        if (grupo == null || grupo.getId() == null) {
            throw new WebApplicationException("Grupo was invalidly set on request.", 422);
        }

        return grupoAlimentoRepository.persist(grupo);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Integer> update(Long id, GrupoAlimento grupo) {
        if (id == null || grupo == null || grupo.getId() == null) {
            throw new WebApplicationException("Id or Grupo was invalidly set on request.", 422);
        }

        Map<String, Object> params = Parameters
                .with("nome", grupo.getNome())
                .and("id", id)
                .map();
        return grupoAlimentoRepository.update(
                "UPDATE grupo_alimento SET nome=:nome WHERE id=:id",
                params);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Long> delete(Long id) {
        if (id == null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        Map<String, Object> params = Parameters
                .with("id", id)
                .map();
        return grupoAlimentoRepository.delete(
                "DELETE grupo_alimento WHERE id=:id",
                params);
    }

}
