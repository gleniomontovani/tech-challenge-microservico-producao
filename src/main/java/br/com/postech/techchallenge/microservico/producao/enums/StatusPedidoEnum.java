package br.com.postech.techchallenge.microservico.producao.enums;

public enum StatusPedidoEnum {

	DESCONHECIDO(0, "Desconhecido"),
	EM_PREPARACAO(4, "Em preparação"),
	PRONTO(5, "Pronto");
	
	private Integer value;
	private String descricao;
	
	private StatusPedidoEnum(Integer value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public Integer getValue() {
		return value;
	}
	

	public String getDescricao() {
		return descricao;
	}

	public static StatusPedidoEnum get(Integer value) {
		for (StatusPedidoEnum status : StatusPedidoEnum.values()) {
			if(status.getValue() == value ) {
				return status;
			}
		}
		return StatusPedidoEnum.DESCONHECIDO;
	}
	
	public static StatusPedidoEnum getByDescricao(String descricao) {
		for (StatusPedidoEnum status : StatusPedidoEnum.values()) {
			if(status.getDescricao().equals(descricao)) {
				return status;
			}
		}
		return StatusPedidoEnum.DESCONHECIDO;
	}
}
