package br.com.robertoxavier.dto.fotoPessoa;

import br.com.robertoxavier.service.Resource;

import java.util.List;

public record FotoRequest(
        Long pesId,
        Resource foto

)
{}
