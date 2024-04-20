package br.com.postech.techchallenge.microservico.core.producao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "producao")
@RequiredArgsConstructor
public class ProducaoController {

	public ResponseEntity<Object> atualizarStatusPedido(){
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	public ResponseEntity<Object> listaSituacaoPedido(){
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
