package br.com.robertoxavier.api.mappers.lotacao;

import br.com.robertoxavier.api.mappers.pessoa.PessoaMapper;
import br.com.robertoxavier.api.mappers.unidade.UnidadeMapper;
import br.com.robertoxavier.data.entities.LotacaoEntity;
import br.com.robertoxavier.dto.lotacao.LotacaoRequest;
import br.com.robertoxavier.dto.lotacao.LotacaoResponse;
import br.com.robertoxavier.model.LotacaoModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LotacaoMapper {

    private final PessoaMapper pessoaMapper;

    private final UnidadeMapper unidadeMapper;

    public LotacaoMapper(PessoaMapper pessoaMapper, UnidadeMapper unidadeMapper) {
        this.pessoaMapper = pessoaMapper;
        this.unidadeMapper = unidadeMapper;
    }

    public LotacaoResponse lotacaoModelToResponse(LotacaoModel lotacaoModel){

        if (lotacaoModel == null) {
            return null;
        }

        return new LotacaoResponse(
                lotacaoModel.getLotId(),
                lotacaoModel.getLotDataLotacao(),
                lotacaoModel.getLotDataRemocao(),
                lotacaoModel.getLotPortaria(),
                pessoaMapper.pessoaModelToResponse(lotacaoModel.getPessoaModel()),
                unidadeMapper.unidadeModelToResponse(lotacaoModel.getUnidadeModel())
        );

    }

    public LotacaoModel lotacaoRequestToModel(LotacaoRequest lotacaoRequest){
       if (lotacaoRequest == null) {
            return null;
        }

        return new LotacaoModel(
                lotacaoRequest.lotDataLotacao(),
                lotacaoRequest.lotDataRemocao(),
                lotacaoRequest.lotPortaria(),
                lotacaoRequest.pesId(),
                lotacaoRequest.unidId()
        );
    }

    public LotacaoEntity lotacaoModelToEntity(LotacaoModel lotacaoModel){
        if (lotacaoModel == null) {
            return null;
        }

        return new LotacaoEntity(
                lotacaoModel.getLotId(),
                lotacaoModel.getLotDataLotacao(),
                lotacaoModel.getLotDataRemocao(),
                lotacaoModel.getLotPortaria(),
                pessoaMapper.pessoaModelToEntity(lotacaoModel.getPessoaModel()),
                unidadeMapper.unidadeModelToEntity(lotacaoModel.getUnidadeModel())
        );
    }

    public LotacaoModel lotacaoEntityToModel(LotacaoEntity lotacaoEntity){
        if (lotacaoEntity == null) {
            return null;
        }

        return new LotacaoModel(
                lotacaoEntity.getLotId(),
                lotacaoEntity.getLotDataLotacao(),
                lotacaoEntity.getLotDataRemocao(),
                lotacaoEntity.getLotPortaria(),
                pessoaMapper.pessoaEntityToModel(lotacaoEntity.getPessoa()),
                unidadeMapper.unidadeEntityToModel(lotacaoEntity.getUnidade())
        );

    }

    public List<LotacaoModel> lotacaoEntityListToLotacaoModelList(List<LotacaoEntity>LotacaoEntityList){
        if (LotacaoEntityList == null) {
            return null;
        }

        return LotacaoEntityList.stream()
                .map(this::lotacaoEntityToModel)
                .collect(Collectors.toList());
    }

    public List<LotacaoResponse> lotacaoModelListToLotacaoResponseList(List<LotacaoModel>LotacaoModelList){
        if (LotacaoModelList == null) {
            return null;
        }

        return LotacaoModelList.stream()
                .map(this::lotacaoModelToResponse)
                .collect(Collectors.toList());
    }
}
