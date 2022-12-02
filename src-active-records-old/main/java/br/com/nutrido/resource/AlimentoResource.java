package br.com.nutrido.resource;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
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

import br.com.nutrido.model.Alimento;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/v1/alimentos")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AlimentoResource {

    @GET
    public Uni<Response> getAlimentos() {
        return Alimento.getAllAlimentos()
                .onItem().transform(alimentos -> Response.ok(alimentos))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleAlimento(@PathParam("id") Long id) {
        return Alimento.findByAlimentoId(id)
                .onItem().ifNotNull().transform(alimento -> Response.ok(alimento).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> add(Alimento alimento) {
        return Alimento.addAlimento(alimento)
                .onItem().transform(id -> URI.create("/v1/alimentos/" + id.getId()))
                .onItem().transform(uri -> Response.created(uri))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> update(@PathParam("id") Long id, Alimento alimento) {
        if (alimento == null || alimento.getNome() == null) {
            throw new WebApplicationException("Alimento was not set on request.", 422);
        }
        return Alimento.updateAlimento(id, alimento)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Alimento.deleteAlimento(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(Status.NOT_FOUND).build()
                        : Response.ok().status(Status.OK).build());
    }

}
