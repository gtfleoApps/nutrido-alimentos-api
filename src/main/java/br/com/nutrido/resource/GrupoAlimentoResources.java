package br.com.nutrido.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.nutrido.model.GrupoAlimento;
import br.com.nutrido.repository.GrupoAlimentoRepository;
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
}
