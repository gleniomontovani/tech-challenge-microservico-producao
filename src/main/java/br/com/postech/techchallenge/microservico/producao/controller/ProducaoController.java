package br.com.postech.techchallenge.microservico.producao.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.service.ProducaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/producao")
@RequiredArgsConstructor
public class ProducaoController {

	private final ProducaoService producaoService;
	
	@GetMapping(path = "/situacao/{situacao}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<List<ProducaoResponse>> listarTodasProducaoPorSituacao(@PathVariable Integer situacao){		
		return new ResponseEntity<>(producaoService.listarTodasProducaoPorSituacao(situacao), HttpStatus.OK);
	}
	
	@GetMapping(path = "/{numeroPedido}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<ProducaoResponse> buscarProducaoPorPedido(@PathVariable Long numeroPedido){		
		return new ResponseEntity<>(producaoService.buscarProducaoPorNumeroPedido(numeroPedido), HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<ProducaoResponse> salvarProducaoPedido(@RequestBody @Valid ProducaoRequest producaoRequest){	
		return new ResponseEntity<>(producaoService.salvarProducaoPedido(producaoRequest), HttpStatus.CREATED);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<ProducaoResponse> atualizarStatusProducao(@RequestBody @Valid ProducaoRequest producaoRequest){	
		return new ResponseEntity<>(producaoService.atualizarStatusProducao(producaoRequest), HttpStatus.OK);
	}
}
