package br.com.postech.techchallenge.microservico.producao.service.integracao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {

	private Long numeroPedido;
	private Long numeroPagamento;
    private String dataPedido;
    private Integer statusPedido;
    private Integer statusPagamento;
    private String qrCodePix;
}
