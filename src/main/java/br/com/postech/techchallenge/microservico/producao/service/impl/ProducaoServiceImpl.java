package br.com.postech.techchallenge.microservico.producao.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import br.com.postech.techchallenge.microservico.producao.configuration.ModelMapperConfiguration;
import br.com.postech.techchallenge.microservico.producao.converts.SituacaoProducaoParaStringConverter;
import br.com.postech.techchallenge.microservico.producao.domain.ProducaoDocumento;
import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.enums.StatusPedidoEnum;
import br.com.postech.techchallenge.microservico.producao.exception.BusinessException;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.repository.ProducaoJpaRepository;
import br.com.postech.techchallenge.microservico.producao.repository.ProducaoMongoRepository;
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
	private final ProducaoJpaRepository producaoJpaRepository;
	private final ProducaoMongoRepository producaoMongoRepository;
	private final ApiMicroServicePedido apiMicroServicePedido;

	@Override
	public List<ProducaoResponse> listarTodasProducaoPorSituacao(Integer situacao) throws BusinessException {
		List<Producao> producoesPorSituacao = producaoJpaRepository
				.findBySituacaoProducao(SituacaoProducaoEnum.get(situacao));
		
		MAPPER.typeMap(Producao.class, ProducaoResponse.class)
				.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
						.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
		
		
		return MAPPER.map(producoesPorSituacao, new TypeToken<List<ProducaoResponse>>() {
		}.getType());
	}

	@Override
	public ProducaoResponse buscarProducaoPorNumeroPedido(Long numeroPedido) throws BusinessException {
		var producaoDocumento = producaoMongoRepository.findByNumeroPedido(numeroPedido);
		
		if (!producaoDocumento.isPresent()) {
			var producao = producaoJpaRepository
					.findByNumeroPedido(numeroPedido)
					.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
			
			MAPPER.typeMap(Producao.class, ProducaoResponse.class)
					.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
							.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
			
			return MAPPER.map(producao, ProducaoResponse.class);		
		}
		
		return MAPPER.map(producaoDocumento, ProducaoResponse.class);
	}

	@Override
	public ProducaoResponse atualizarStatusProducao(ProducaoRequest producaoRequest) throws BusinessException {
		var producao = producaoJpaRepository
				.findByNumeroPedido(producaoRequest.numeroPedido())
				.orElseThrow(() -> new BusinessException("Pedido não encontrado!"));
		
		producao.setSituacaoProducao(SituacaoProducaoEnum.get(producaoRequest.situacaoProducao()));
		producao.setObservacao(producaoRequest.observacao());
		producao.setDataInicioPreparo(obterDataInicioPreparoProducao(producao, producaoRequest.situacaoProducao()));
		producao.setDataFimPreparo(obterDataFimPreparoProducao(producao, producaoRequest.situacaoProducao()));
		
		Integer statusPedido = obterStatusPedido(producaoRequest.situacaoProducao());
		
		
		producao = producaoJpaRepository.save(producao);
		PedidoResponse response = apiMicroServicePedido.atualizarPedido(new PedidoRequest(producao.getNumeroPedido(), statusPedido));
		var producaoResponse = MAPPER.map(producao, ProducaoResponse.class);
		producaoResponse.setStatusPedido(StatusPedidoEnum.get(response.getStatusPedido()).getDescricao());
		
		var producaoDocumento = MAPPER.map(producaoResponse, ProducaoDocumento.class);
		producaoMongoRepository.save(producaoDocumento);
		
		return producaoResponse;
	}

	@Override
	public ProducaoResponse salvarProducaoPedido(ProducaoRequest producaoRequest) throws BusinessException {
		var producaoEntity = producaoJpaRepository
			.findByNumeroPedido(producaoRequest.numeroPedido())
			.orElse(null);
		
		if(Objects.isNull(producaoEntity)) {
			var producao = MAPPER.map(producaoRequest, Producao.class);
			producao.setSituacaoProducao(SituacaoProducaoEnum.RECEBIDO);
			producao.setDataInicioPreparo(obterDataInicioPreparoProducao(producao, producaoRequest.situacaoProducao()));
			
			producaoEntity = producaoJpaRepository.save(producao);
		}
		
		MAPPER.typeMap(Producao.class, ProducaoResponse.class)
				.addMappings(mapperA -> mapperA.using(new SituacaoProducaoParaStringConverter())
						.map(Producao::getSituacaoProducao, ProducaoResponse::setStatusPedido));
		
		var producaoResponse = MAPPER.map(producaoEntity, ProducaoResponse.class);
		var producaoDocumento = MAPPER.map(producaoResponse, ProducaoDocumento.class);
		
		producaoMongoRepository.save(producaoDocumento);
		
		return producaoResponse;
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
