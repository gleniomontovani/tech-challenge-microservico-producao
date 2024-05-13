package br.com.postech.techchallenge.microservico.producao.converts;

import org.modelmapper.AbstractConverter;

import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;

public class InteiroParaSituacaoProducaoConverter extends AbstractConverter<Integer, SituacaoProducaoEnum> {

	@Override
	protected SituacaoProducaoEnum convert(Integer source) {
		return SituacaoProducaoEnum.get(source);
	}

}
