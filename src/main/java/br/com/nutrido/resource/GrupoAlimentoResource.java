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

import br.com.nutrido.model.GrupoAlimento;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/v1/grupos")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class GrupoAlimentoResource {

    @GET
    public Uni<Response> getGruposDeAlimentos() {
        return GrupoAlimento.getAllGrupos()
                .onItem().transform(grupos -> Response.ok(grupos))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleGrupoDeAlimentos(@PathParam("id") Long id) {
        return GrupoAlimento.findByGrupoId(id)
                .onItem().ifNotNull().transform(grupo -> Response.ok(grupo).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> add(GrupoAlimento grupo) {
        return GrupoAlimento.addGrupo(grupo)
                .onItem().transform(id -> URI.create("/v1/grupos/" + id.getId()))
                .onItem().transform(uri -> Response.created(uri))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> update(@PathParam("id") Long id, GrupoAlimento grupo) {
        if (grupo == null || grupo.getNome() == null) {
            throw new WebApplicationException("Grupo de Alimento was not set on request.", 422);
        }
        return GrupoAlimento.updateGrupo(id, grupo)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        return GrupoAlimento.deleteGrupo(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(Status.NOT_FOUND).build()
                        : Response.ok().status(Status.OK).build());
    }

}
