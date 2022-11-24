package br.com.nutrido.resource;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
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
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/v1/medidas")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UnidadeMedidaResource {

    @GET
    public Uni<Response> getUnidadesDeMedida() {
        return UnidadeMedida.getAllMedidas()
                .onItem().transform(medidas -> Response.ok(medidas))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleUnidadeDeMedida(@PathParam("id") Long id) {
        return UnidadeMedida.findByMedidaId(id)
                .onItem().ifNotNull().transform(medida -> Response.ok(medida).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> add(UnidadeMedida medida) {
        return UnidadeMedida.addMedida(medida)
                .onItem().transform(id -> URI.create("/v1/medidas/" + id.getId()))
                .onItem().transform(uri -> Response.created(uri))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> update(@PathParam("id") Long id, UnidadeMedida medida) {
        if (medida == null || medida.getNome() == null || medida.getNomeAbreviado() == null) {
            throw new WebApplicationException("Unidade de Medida was not set on request.", 422);
        }
        return UnidadeMedida.updateMedida(id, medida)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        return UnidadeMedida.deleteMedida(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(Status.NOT_FOUND).build()
                        : Response.ok().status(Status.OK).build());
    }
}
