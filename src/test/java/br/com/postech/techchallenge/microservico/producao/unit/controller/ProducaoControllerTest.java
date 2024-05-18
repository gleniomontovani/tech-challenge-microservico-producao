package br.com.postech.techchallenge.microservico.producao.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.postech.techchallenge.microservico.producao.controller.ProducaoController;
import br.com.postech.techchallenge.microservico.producao.handler.RestHandlerException;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.service.ProducaoService;
import br.com.postech.techchallenge.microservico.producao.util.Constantes;
import br.com.postech.techchallenge.microservico.producao.util.ObjectCreatorHelper;
import br.com.postech.techchallenge.microservico.producao.util.Utilitario;

class ProducaoControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private ProducaoService producaoService;
	
	AutoCloseable openMocks;

	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		ProducaoController producaoController = new ProducaoController(producaoService);
		mockMvc = MockMvcBuilders.standaloneSetup(producaoController)
		        .setControllerAdvice(new RestHandlerException())
		        .addFilter((request, response, chain) -> {
		          response.setCharacterEncoding(Constantes.UTF_8);
		          chain.doFilter(request, response);
		        }, "/*")
		        .build();
	}
	
	@AfterEach
	void close() throws Exception {
		openMocks.close();
	}

	@Test
	void devePermitirListarTodasProducaoPorSituacao() throws Exception {
		var producaoResponse1 = ObjectCreatorHelper.obterProducaoResponse();
		producaoResponse1.setNumeroPedido(1L);
		
		var producaoResponse2 = ObjectCreatorHelper.obterProducaoResponse();
		producaoResponse2.setNumeroPedido(2L);
		
		List<ProducaoResponse> pedidosResponse = Arrays.asList(producaoResponse1, producaoResponse2);
		
		when(producaoService.listarTodasProducaoPorSituacao(anyInt())).thenReturn(pedidosResponse);
		
		mockMvc.perform(get("/v1/producao/situacao/{situacao}", 1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.[0].numeroPedido").value(producaoResponse1.getNumeroPedido()))
        .andExpect(jsonPath("$.[0].dataInicioPreparo").value(producaoResponse1.getDataInicioPreparo()))
        .andExpect(jsonPath("$.[0].statusPedido").value(producaoResponse1.getStatusPedido()))
        .andExpect(jsonPath("$.[0].observacao").value(producaoResponse1.getObservacao()));
		
		verify(producaoService, times(1)).listarTodasProducaoPorSituacao(anyInt());
	}

	@Test
	void devePermitirBuscarProducaoPorPedido() throws Exception {
		var producaoResponse = ObjectCreatorHelper.obterProducaoResponse();
		producaoResponse.setNumeroPedido(1L);		

		when(producaoService.buscarProducaoPorNumeroPedido(anyLong())).thenReturn(producaoResponse);
		
		mockMvc.perform(get("/v1/producao/{numeroPedido}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.numeroPedido").value(producaoResponse.getNumeroPedido()))
        .andExpect(jsonPath("$.dataInicioPreparo").value(producaoResponse.getDataInicioPreparo()))
        .andExpect(jsonPath("$.statusPedido").value(producaoResponse.getStatusPedido()))
        .andExpect(jsonPath("$.observacao").value(producaoResponse.getObservacao()));
		
		verify(producaoService, times(1)).buscarProducaoPorNumeroPedido(anyLong());
	}

	@Test
	void devePermitirSalvarProducaoPedido() throws Exception {
		var producaoRequest = ObjectCreatorHelper.obterProducaoRequest();
		
		var producaoResponse = ObjectCreatorHelper.obterProducaoResponse();
		producaoResponse.setNumeroPedido(1L);
		
		when(producaoService.salvarProducaoPedido(any(ProducaoRequest.class))).thenReturn(producaoResponse);
		
		mockMvc.perform(post("/v1/producao")
				.contentType(MediaType.APPLICATION_JSON)
				.content(Utilitario.asJsonString(producaoRequest)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.numeroPedido").value(producaoResponse.getNumeroPedido()))
        .andExpect(jsonPath("$.dataInicioPreparo").value(producaoResponse.getDataInicioPreparo()))
        .andExpect(jsonPath("$.statusPedido").value(producaoResponse.getStatusPedido()))
        .andExpect(jsonPath("$.observacao").value(producaoResponse.getObservacao()));
		
		verify(producaoService, times(1)).salvarProducaoPedido(any(ProducaoRequest.class));
	}

	@Test
	void devePermitirAtualizarStatusProducao() throws Exception {
		var producaoRequest = ObjectCreatorHelper.obterProducaoRequest();
		
		var producaoResponse = ObjectCreatorHelper.obterProducaoResponse();
		producaoResponse.setNumeroPedido(1L);
		
		when(producaoService.atualizarStatusProducao(any(ProducaoRequest.class))).thenReturn(producaoResponse);
		
		mockMvc.perform(put("/v1/producao")
				.contentType(MediaType.APPLICATION_JSON)
				.content(Utilitario.asJsonString(producaoRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.numeroPedido").value(producaoResponse.getNumeroPedido()))
        .andExpect(jsonPath("$.dataInicioPreparo").value(producaoResponse.getDataInicioPreparo()))
        .andExpect(jsonPath("$.statusPedido").value(producaoResponse.getStatusPedido()))
        .andExpect(jsonPath("$.observacao").value(producaoResponse.getObservacao()));
		
		verify(producaoService, times(1)).atualizarStatusProducao(any(ProducaoRequest.class));
	}
}
