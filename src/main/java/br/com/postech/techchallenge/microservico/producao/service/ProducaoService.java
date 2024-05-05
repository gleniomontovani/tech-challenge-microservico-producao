package br.com.postech.techchallenge.microservico.producao.service;

import java.util.List;

import br.com.postech.techchallenge.microservico.producao.exception.BusinessException;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;

public interface ProducaoService {

	List<ProducaoResponse> listarTodasProducaoPorSituacao(Integer situacao) throws BusinessException;
	ProducaoResponse buscarProducaoPorNumeroPedido(Long numeroPedido) throws BusinessException;
	ProducaoResponse salvarProducaoPedido(ProducaoRequest producaoRequest) throws BusinessException;
	ProducaoResponse atualizarStatusProducao(ProducaoRequest producaoRequest) throws BusinessException;
}
