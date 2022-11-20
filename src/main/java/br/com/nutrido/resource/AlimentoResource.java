package br.com.nutrido.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import br.com.nutrido.dto.AlimentoDTO;
import br.com.nutrido.model.Alimento;
import br.com.nutrido.model.GrupoAlimento;
import br.com.nutrido.model.UnidadeMedida;
import br.com.nutrido.repository.AlimentoRepository;
import br.com.nutrido.repository.GrupoAlimentoRepository;
import br.com.nutrido.repository.UnidadeMedidaRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
// import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

@Path("/alimentos")
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
    public Uni<List<Alimento>> get() {
        return alimentoRepository.listAll(Sort.by("nome"));
    }

    @GET
    @Path("{id}")
    public Uni<Alimento> getSingle(Long id) {
        return alimentoRepository.findById(id);
    }

    @POST
    @Transactional
    // @ReactiveTransactional
    public Uni<Alimento> create(AlimentoDTO alimentoDto) {
        if (alimentoDto == null) {
            throw new WebApplicationException("Alimento was invalidly set on request.", 422);
        }

        Alimento alimento = new Alimento();
        alimento.setNome(alimentoDto.getNome());
        alimento.setKcal(alimentoDto.getKcal());
        alimento.setProteina(alimentoDto.getProteina());
        alimento.setLipidio(alimentoDto.getLipidio());
        alimento.setCarboidrato(alimentoDto.getCarboidrato());
        alimento.setFibra(alimentoDto.getFibra());
        alimento.setQuantidade(alimentoDto.getQuantidade());

        // ***************** Grupo de Alimento:
        Uni<GrupoAlimento> uniGrupo = grupoAlimentoRepository
                .findById(alimentoDto.getGrupoAlimentoId());

        alimento.setGrupoAlimento(uniGrupo.await().indefinitely());
        // uniGrupo.subscribe().with(
        // result -> {
        // System.out.println("Resultado: " + result);
        // alimento.setGrupoAlimento(result);

        // },
        // failure -> failure.printStackTrace());

        System.out.println(alimento.getGrupoAlimento().toString());
        if (alimento.getGrupoAlimento() == null) {
            throw new WebApplicationException("Grupo do Alimento was invalidly set on request.", 422);
        }

        // ***************** Unidade de Medida:
        Uni<UnidadeMedida> uniMedida = unidadeMedidaRepository
                .findById(alimentoDto.getUnidadeMedidaId());

        alimento.setUnidadeMedida(uniMedida.await().indefinitely());
        // uniMedida.subscribe().with(
        // result -> {
        // System.out.println("Resultado: " + result);
        // alimento.setUnidadeMedida(result);
        // },
        // failure -> failure.printStackTrace());

        System.out.println(alimento.getUnidadeMedida().toString());
        if (alimento.getUnidadeMedida() == null) {
            throw new WebApplicationException("Medida do Alimento was invalidly set on request.", 422);
        }

        // return alimentoRepository.persist(alimento);
        return Panache.withTransaction(() -> alimentoRepository.persist(alimento));
    }
}
