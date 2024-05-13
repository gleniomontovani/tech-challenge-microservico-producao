package br.com.postech.techchallenge.microservico.producao.converts;

import org.modelmapper.AbstractConverter;

import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;

public class SituacaoProducaoParaInteiroConverter extends AbstractConverter<SituacaoProducaoEnum, Integer> {

	@Override
	protected Integer convert(SituacaoProducaoEnum source) {
		return source == null ? null : source.getValue();
	}
}
