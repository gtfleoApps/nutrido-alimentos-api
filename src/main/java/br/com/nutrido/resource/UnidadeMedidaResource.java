package br.com.nutrido.resource;

import java.net.URI;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.nutrido.model.UnidadeMedida;
import br.com.nutrido.repository.UnidadeMedidaRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Path("/v1/medidas")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UnidadeMedidaResource {

    @Inject
    UnidadeMedidaRepository unidadeMedidaRepository;

    @GET
    public Uni<Response> getUnidadesMedida() {
        return unidadeMedidaRepository.getAllMedidas()
                .onItem().transform(unidades -> Response.ok(unidades))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleUnidadeMedida(Long id) {
        return unidadeMedidaRepository.findByMedidaId(id)
                .onItem().ifNotNull().transform(unidade -> Response.ok(unidade).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> add(UnidadeMedida medida) {
        if (medida == null || medida.getId() == null) {
            throw new WebApplicationException("Medida was invalidly set on request.", 422);
        }

        return unidadeMedidaRepository.addMedida(medida)
                .onItem().transform(id -> URI.create("/v1/medidas/" + id.getId()))
                .onItem().transform(uri -> Response.created(uri))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> update(@PathParam("id") Long id, UnidadeMedida medida) {
        if (id == null || medida == null || medida.getNome() == null) {
            throw new WebApplicationException("Id or Medida was invalidly set on request.", 422);
        }

        // Map<String, Object> params = Parameters
        // .with("nome", medida.getNome())
        // .and("nomeAbreviado", medida.getNomeAbreviado())
        // .and("id", id)
        // .map();
        // return unidadeMedidaRepository.update(
        // "UPDATE unidade_medida SET nome=:nome, nome_abreviado=:nomeAbreviado WHERE
        // id=:id",
        // params);
        return unidadeMedidaRepository.updateMedida(id, medida)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        if (id == null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        // Map<String, Object> params = Parameters
        // .with("id", id)
        // .map();
        // return unidadeMedidaRepository.delete(
        // "DELETE unidade_medida WHERE id=:id",
        // params);
        return unidadeMedidaRepository.deleteMedida(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(Status.NOT_FOUND).build()
                        : Response.ok().status(Status.OK).build());
    }
}
