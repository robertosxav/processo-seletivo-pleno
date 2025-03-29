package br.com.robertoxavier.api.controllers;

import br.com.robertoxavier.PageQuery;
import br.com.robertoxavier.PageResponse;
import br.com.robertoxavier.api.mappers.cidade.CidadeMapper;
import br.com.robertoxavier.dto.cidade.CidadeRequest;
import br.com.robertoxavier.dto.cidade.CidadeResponse;
import br.com.robertoxavier.dto.lotacao.LotacaoRequest;
import br.com.robertoxavier.model.CidadeModel;
import br.com.robertoxavier.stories.cidade.CidadeUseStory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Cidades")
@RequestMapping("/cidade")
public class CidadeController {

    private final CidadeMapper cidadeMapper;
    private final CidadeUseStory cidadeUseStory;

    public CidadeController(CidadeMapper cidadeMapper,
                            CidadeUseStory cidadeUseStory) {
        this.cidadeMapper = cidadeMapper;
        this.cidadeUseStory = cidadeUseStory;
    }

    @Operation(
            summary = "Criar uma nova cidade",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CidadeRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo",
                                    value = """
                    {
                      "cidNome": "Cuiaba",
                      "cidUf": "MT"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Cidade criada com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada")
    })
    @PostMapping()
    public CidadeResponse criarCidade(@RequestBody CidadeRequest cidadeRequest) {
        return cidadeMapper.cidadeModelToResponse(cidadeUseStory
                .criar(cidadeMapper.cidadeRequestToModel(cidadeRequest)));
    }

    @Operation(
            summary = "Atualizar uma cidade pelo Id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CidadeRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo",
                                    value = """
                    {
                      "cidNome": "Cuiaba",
                      "cidUf": "MT"
                    
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Cidade atualizada com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @PutMapping("/{cidId}")
    public CidadeResponse atualizarCidade(@PathVariable Long cidId,
                                          @RequestBody CidadeRequest cidadeRequest) {
        return cidadeMapper.cidadeModelToResponse(cidadeUseStory
                .atualizar(cidId,cidadeMapper.cidadeRequestToModel(cidadeRequest)));
    }

    @Operation(summary = "Excluir uma cidade pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Cidade excluida com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @DeleteMapping("/{cidId}")
    public ResponseEntity<String> excluir(@PathVariable Long cidId) {
        cidadeUseStory.excluir(cidId);
        return ResponseEntity.ok("Cidade excluida com sucesso");
    }

    @Operation(summary = "Buscar uma cidade pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Cidade buscada pelo Id com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @GetMapping("/{cidId}")
    public CidadeResponse buscarCidadePorId(@PathVariable Long cidId) {
        return cidadeMapper.cidadeModelToResponse(cidadeUseStory
                .buscarPorId(cidId));
    }

    @Operation(summary = "Listar cidades de forma paginado")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Cidades listadas de forma paginado"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @GetMapping("/paginado/all")
    public PageResponse<CidadeResponse> listaCidadesPaginado(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int sizePage) {
        PageQuery pageQuery = new PageQuery(page, sizePage);
        PageResponse<CidadeModel> cidadePage = cidadeUseStory.listaCidadesPaginado(pageQuery);

        return cidadePage.map(cidadeMapper::cidadeModelToResponse);
    }
}