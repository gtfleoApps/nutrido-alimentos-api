package br.com.nutrido.resource;

import java.net.URI;

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

import br.com.nutrido.model.Alimento;
import br.com.nutrido.repository.AlimentoRepository;
import br.com.nutrido.repository.GrupoAlimentoRepository;
import br.com.nutrido.repository.UnidadeMedidaRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/v1/alimentos")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AlimentoResource {

    @Inject
    AlimentoRepository alimentoRepository;

    @Inject
    GrupoAlimentoRepository grupoAlimentoRepository;

    @Inject
    UnidadeMedidaRepository unidadeMedidaRepository;

    @GET
    public Uni<Response> getAlimentos() {
        return alimentoRepository.getAllAlimentos()
                .onItem().transform(alimentos -> Response.ok(alimentos))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSinglAlimento(@PathParam("id") Long id) {
        return alimentoRepository.findByAlimentoId(id)
                .onItem().ifNotNull().transform(alimento -> Response.ok(alimento).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @GET
    @Path("/grupo/{id}")
    public Uni<Response> getAlimentosByGrupo(@PathParam("id") Long id) {
        return alimentoRepository.findByGrupoAlimentoId(id)
                .onItem().transform(alimentos -> Response.ok(alimentos))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> add(Alimento alimento) {
        return alimentoRepository.addAlimento(alimento)
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
        return alimentoRepository.updateAlimento(id, alimento)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Status.NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        return alimentoRepository.deleteAlimento(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(Status.NOT_FOUND).build()
                        : Response.ok().status(Status.OK).build());
    }

    /*
     * @POST
     * 
     * @Transactional
     * public Uni<Alimento> create(AlimentoDTO alimentoDto) {
     * if (alimentoDto == null) {
     * throw new WebApplicationException("Alimento was invalidly set on request.",
     * 422);
     * }
     * 
     * Alimento alimento = new Alimento();
     * alimento.setNome(alimentoDto.getNome());
     * alimento.setKcal(alimentoDto.getKcal());
     * alimento.setProteina(alimentoDto.getProteina());
     * alimento.setLipidio(alimentoDto.getLipidio());
     * alimento.setCarboidrato(alimentoDto.getCarboidrato());
     * alimento.setFibra(alimentoDto.getFibra());
     * alimento.setQuantidade(alimentoDto.getQuantidade());
     * 
     * // ***************** Grupo de Alimento:
     * Uni<GrupoAlimento> uniGrupo = grupoAlimentoRepository
     * .findById(alimentoDto.getGrupoAlimentoId());
     * 
     * alimento.setGrupoAlimento(uniGrupo.await().indefinitely());
     * // uniGrupo.subscribe().with(
     * // grupo -> {
     * // System.out.println("RESULTADO: " + grupo);
     * // alimento.setGrupoAlimento(grupo);
     * // },
     * // failure -> failure.printStackTrace());
     * 
     * // ***************** Unidade de Medida:
     * Uni<UnidadeMedida> uniMedida = unidadeMedidaRepository
     * .findById(alimentoDto.getUnidadeMedidaId());
     * 
     * alimento.setUnidadeMedida(uniMedida.await().indefinitely());
     * // uniMedida.subscribe().with(
     * // unidade -> {
     * // System.out.println("RESULTADO: " + unidade);
     * // alimento.setUnidadeMedida(unidade);
     * // },
     * // failure -> failure.printStackTrace());
     * 
     * if (alimento.getUnidadeMedida() == null ||
     * alimento.getUnidadeMedida().getId() == null) {
     * return Uni.createFrom().failure(
     * new
     * WebApplicationException("Medida do Alimento was invalidly set on request.",
     * 422));
     * 
     * } else if (alimento.getGrupoAlimento() == null ||
     * alimento.getGrupoAlimento().getId() == null) {
     * return Uni.createFrom().failure(
     * new
     * WebApplicationException("Grupo do Alimento was invalidly set on request.",
     * 422));
     * }
     * 
     * // return alimentoRepository.persist(alimento);
     * // return Panache.withTransaction(() ->
     * alimentoRepository.persist(alimento));
     * return Panache.withTransaction(() -> alimentoRepository.persist(alimento));
     * }
     */

}
