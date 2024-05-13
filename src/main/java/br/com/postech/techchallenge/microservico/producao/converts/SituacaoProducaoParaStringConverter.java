package br.com.postech.techchallenge.microservico.producao.converts;

import org.modelmapper.AbstractConverter;

import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;

public class SituacaoProducaoParaStringConverter extends AbstractConverter<SituacaoProducaoEnum, String> {

	@Override
	protected String convert(SituacaoProducaoEnum source) {
		return source == null ? null : source.getDescricao();
	}

}
