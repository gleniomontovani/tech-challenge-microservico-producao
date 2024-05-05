package br.com.postech.techchallenge.microservico.producao.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import br.com.postech.techchallenge.microservico.producao.configuration.ModelMapperConfiguration;
import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.exception.BusinessException;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.repository.ProducaoRepository;
import br.com.postech.techchallenge.microservico.producao.service.ProducaoService;
import br.com.postech.techchallenge.microservico.producao.service.integracao.ApiMicroServicePedido;
import br.com.postech.techchallenge.microservico.producao.service.integracao.request.PedidoRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProducaoServiceImpl implements ProducaoService{
	private static final ModelMapper MAPPER = ModelMapperConfiguration.getModelMapper();
	private final ProducaoRepository producaoRepository;
	private final ApiMicroServicePedido apiMicroServicePedido;

	@Override
	public List<ProducaoResponse> listarTodasProducaoPorSituacao(Integer situacao) throws BusinessException {
		List<Producao> producoesPorSituacao = producaoRepository
				.findBySituacaoProducao(SituacaoProducaoEnum.get(situacao));
		
		
		return MAPPER.map(producoesPorSituacao, new TypeToken<List<ProducaoResponse>>() {
		}.getType());
	}

	@Override
	public ProducaoResponse buscarProducaoPorNumeroPedido(Long numeroPedido) throws BusinessException {
		var producao = producaoRepository
				.findByNumeroPedido(numeroPedido)
				.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
		
		
		return MAPPER.map(producao, ProducaoResponse.class);
	}

	@Override
	public ProducaoResponse atualizarStatusProducao(ProducaoRequest producaoRequest) throws BusinessException {
		var producao = producaoRepository
				.findByNumeroPedido(producaoRequest.numeroPedido())
				.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
		
		producao.setSituacaoProducao(SituacaoProducaoEnum.get(producaoRequest.situacaoProducao()));
		producao.setObservacao(producaoRequest.observacao());
		
		Integer statusPedido = producaoRequest.situacaoProducao() == 3 ? 5 : 4;
		
		producao = producaoRepository.save(producao);
		apiMicroServicePedido.atualizarPedido(new PedidoRequest(producao.getNumeroPedido(), statusPedido));
		
		return MAPPER.map(producao, ProducaoResponse.class);
	}

	@Override
	public ProducaoResponse salvarProducaoPedido(ProducaoRequest producaoRequest) throws BusinessException {
		var producao = MAPPER.map(producaoRequest, Producao.class);
		producao.setSituacaoProducao(SituacaoProducaoEnum.RECEBIDO);
		
		producao = producaoRepository.save(producao);
		
		return MAPPER.map(producao, ProducaoResponse.class);
	}
}
