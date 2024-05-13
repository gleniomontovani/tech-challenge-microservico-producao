package br.com.postech.techchallenge.microservico.producao.util;

import java.time.LocalDateTime;

import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;

public class ObjectCreatorHelper {

	public static Producao obterProducao() {
		return Producao.builder()
				.numeroPedido(1L)
				.observacao("Producao")
				.dataInicioPreparo(LocalDateTime.now())
				.situacaoProducao(SituacaoProducaoEnum.RECEBIDO)
				.build();
	}
	
	public static ProducaoRequest obterProducaoRequest() {
		return new ProducaoRequest(1L, "Producao", 1);
	}
	
	public static ProducaoResponse obterProducaoResponse() {
		return ProducaoResponse.builder()
				.dataInicioPreparo(LocalDateTime.now().toString())
				.numeroPedido(1L)
				.observacao("Producao")
				.statusPedido("Recebido")
				.build();
	}
}
