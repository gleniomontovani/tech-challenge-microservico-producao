package br.com.postech.techchallenge.microservico.producao.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import br.com.postech.techchallenge.microservico.producao.configuration.ModelMapperConfiguration;
import br.com.postech.techchallenge.microservico.producao.converts.SituacaoProducaoParaStringConverter;
import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.enums.StatusPedidoEnum;
import br.com.postech.techchallenge.microservico.producao.exception.BusinessException;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.repository.ProducaoRepository;
import br.com.postech.techchallenge.microservico.producao.service.ProducaoService;
import br.com.postech.techchallenge.microservico.producao.service.integracao.ApiMicroServicePedido;
import br.com.postech.techchallenge.microservico.producao.service.integracao.request.PedidoRequest;
import br.com.postech.techchallenge.microservico.producao.service.integracao.response.PedidoResponse;
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
		
		MAPPER.typeMap(Producao.class, ProducaoResponse.class)
				.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
						.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
		
		
		return MAPPER.map(producoesPorSituacao, new TypeToken<List<ProducaoResponse>>() {
		}.getType());
	}

	@Override
	public ProducaoResponse buscarProducaoPorNumeroPedido(Long numeroPedido) throws BusinessException {
		var producao = producaoRepository
				.findByNumeroPedido(numeroPedido)
				.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
		
		MAPPER.typeMap(Producao.class, ProducaoResponse.class)
				.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
						.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
		
		return MAPPER.map(producao, ProducaoResponse.class);
	}

	@Override
	public ProducaoResponse atualizarStatusProducao(ProducaoRequest producaoRequest) throws BusinessException {
		var producao = producaoRepository
				.findByNumeroPedido(producaoRequest.numeroPedido())
				.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
		
		producao.setSituacaoProducao(SituacaoProducaoEnum.get(producaoRequest.situacaoProducao()));
		producao.setObservacao(producaoRequest.observacao());
		producao.setDataFimPreparo(obterDataFimPreparoProducao(producao, producaoRequest.situacaoProducao()));
		
		Integer statusPedido = obterStatusPedido(producaoRequest.situacaoProducao());
		
		
		producao = producaoRepository.save(producao);
		PedidoResponse response = apiMicroServicePedido.atualizarPedido(new PedidoRequest(producao.getNumeroPedido(), statusPedido));
		var producaoResponse = MAPPER.map(producao, ProducaoResponse.class);
		producaoResponse.setStatusPedido(StatusPedidoEnum.get(response.getStatusPedido()).getDescricao());
		
		return producaoResponse;
	}

	@Override
	public ProducaoResponse salvarProducaoPedido(ProducaoRequest producaoRequest) throws BusinessException {
		var producaoEntity = producaoRepository
			.findByNumeroPedido(producaoRequest.numeroPedido())
			.orElse(null);
		
		if(Objects.isNull(producaoEntity)) {
			var producao = MAPPER.map(producaoRequest, Producao.class);
			producao.setSituacaoProducao(SituacaoProducaoEnum.RECEBIDO);
			producao.setDataInicioPreparo(obterDataInicioPreparoProducao(producao, producaoRequest.situacaoProducao()));
			
			producaoEntity = producaoRepository.save(producao);
		}
		
		MAPPER.typeMap(Producao.class, ProducaoResponse.class)
				.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
						.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
		
		return MAPPER.map(producaoEntity, ProducaoResponse.class);
	}
	
	private LocalDateTime obterDataInicioPreparoProducao(Producao producao, Integer situacao) {
		return SituacaoProducaoEnum.get(situacao).equals(SituacaoProducaoEnum.EM_PREPARACAO) 
				? LocalDateTime.now()
				: producao.getDataInicioPreparo();
	}
	
	private LocalDateTime obterDataFimPreparoProducao(Producao producao, Integer situacao) {
		return SituacaoProducaoEnum.get(situacao).equals(SituacaoProducaoEnum.PRONTO)  
				? LocalDateTime.now() 
				: null;
	}
	
	private Integer obterStatusPedido(Integer situacao) {
		return SituacaoProducaoEnum.get(situacao).equals(SituacaoProducaoEnum.PRONTO) 
				? StatusPedidoEnum.PRONTO.getValue() 
				: StatusPedidoEnum.EM_PREPARACAO.getValue();
	}
}
