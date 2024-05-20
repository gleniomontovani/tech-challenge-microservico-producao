package br.com.postech.techchallenge.microservico.producao.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "producao")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducaoDocumento {

	@Id
	private Long id;
	private Long numeroPedido;
	private String observacao;
	private String statusPedido;
	private String dataInicioPreparo;
	private String dataFimPreparo;
}
