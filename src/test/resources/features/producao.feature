# language: pt

Funcionalidade: API Microserviço de Produção

	Cenário: Salvar uma produção
		Quando submeter uma nova produção
		Então a produção é registrada com sucesso
		
	Cenário: Buscar uma produção por número de pedido
		Dado que uma produção já foi cadastrada
		Quando requisitar a busca da produção
		Então a produção é exibida com sucesso
		
	Cenário: Listar por situação da produção
	 Quando requisitar a lista de produção por uma situação
	 Então as produções serão exibidas com sucesso

  Cenário: Atualizar status de uma produção
 		Dado que uma produção já foi cadastrada
 		Quando requisitar a alteração do status da produção
 		Então a produção terá seu status atualizado	 
 		