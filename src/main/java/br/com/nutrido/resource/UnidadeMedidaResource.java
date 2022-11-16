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

import br.com.nutrido.model.UnidadeMedida;
import br.com.nutrido.repository.UnidadeMedidaRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Path("/medidas")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UnidadeMedidaResource {

    @Inject
    UnidadeMedidaRepository unidadeMedidaRepository;

    @GET
    public Uni<List<UnidadeMedida>> get() {
        return unidadeMedidaRepository.listAll(Sort.by("nome"));
    }

    @GET
    @Path("{id}")
    public Uni<UnidadeMedida> getSingle(Long id) {
        return unidadeMedidaRepository.findById(id);
    }

    @POST
    @ReactiveTransactional
    public Uni<UnidadeMedida> create(UnidadeMedida medida) {
        if (medida == null || medida.getId() == null) {
            throw new WebApplicationException("Medida was invalidly set on request.", 422);
        }

        return unidadeMedidaRepository.persist(medida);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Integer> update(Long id, UnidadeMedida medida) {
        if (id == null || medida == null || medida.getId() == null) {
            throw new WebApplicationException("Id or Medida was invalidly set on request.", 422);
        }

        Map<String, Object> params = Parameters
                .with("nome", medida.getNome())
                .and("nomeAbreviado", medida.getNomeAbreviado())
                .and("id", id)
                .map();
        return unidadeMedidaRepository.update(
                "UPDATE unidade_medida SET nome=:nome, nome_abreviado=:nomeAbreviado WHERE id=:id",
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
        return unidadeMedidaRepository.delete(
                "DELETE unidade_medida WHERE id=:id",
                params);
    }
}
