package br.com.postech.techchallenge.microservico.producao.service.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.postech.techchallenge.microservico.producao.configuration.ControllerMappingConfig;
import br.com.postech.techchallenge.microservico.producao.exception.BusinessException;
import br.com.postech.techchallenge.microservico.producao.service.integracao.request.PedidoRequest;
import br.com.postech.techchallenge.microservico.producao.service.integracao.response.PedidoResponse;
import feign.Headers;

@FeignClient(url = "${api.client.pedido.uri}", path = ControllerMappingConfig.PATH_API_PEDIDO, name = "pedidos")
public interface ApiMicroServicePedido {
	
	@RequestMapping(method = RequestMethod.GET, value = "/produtos/{numeroPedido}")
	@Headers("Accept: application/json")
	String consultarProdutosPorPedido(@PathVariable("numeroPedido") Integer numeroPedido) throws BusinessException;
	
	@RequestMapping(method = RequestMethod.PUT)
	PedidoResponse atualizarPedido(@RequestBody PedidoRequest pedidoRequest)throws BusinessException;
}
