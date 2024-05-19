package br.com.postech.techchallenge.microservico.producao.bdd;

import br.com.postech.techchallenge.microservico.producao.configuration.ControllerMappingConfig;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.model.request.ProducaoRequest;
import br.com.postech.techchallenge.microservico.producao.model.response.ProducaoResponse;
import br.com.postech.techchallenge.microservico.producao.util.ObjectCreatorHelper;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StepDefinitionProducao {
	
	private Response response;
	
	private ProducaoResponse producaoResponse;	

	@Before
	public void setup() {
		this.producaoResponse = ProducaoResponse.builder().build();
	}

	// - ###################################################
	// - Salvar Produção
	// - ###################################################
	@Quando("submeter uma nova produção")
	public ProducaoResponse submeter_uma_nova_produção() {
		var producaoRequest = ObjectCreatorHelper.obterProducaoRequest();
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(producaoRequest)
				.when()
				.post(ControllerMappingConfig.ENDPOINT_MICRO_SERVICE_PRODUCAO_LOCAL);		
		
		return response.then().extract().as(ProducaoResponse.class);
	}
	
	@Então("a produção é registrada com sucesso")
	public void a_produção_é_registrada_com_sucesso() {
		response.then()
	        .statusCode(HttpStatus.CREATED.value())
	        .body(matchesJsonSchemaInClasspath("./schemas/ProducaoResponseSchema.json"));
	}
		
	// - ###################################################
	// - Buscar produção por pedido
	// - ###################################################
	@Dado("que uma produção já foi cadastrada")
	public void que_uma_produção_já_foi_cadastrada() {
		this.producaoResponse = submeter_uma_nova_produção();
	}
	
	@Quando("requisitar a busca da produção")
	public void requisitar_a_busca_da_produção() {
		response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ControllerMappingConfig.ENDPOINT_MICRO_SERVICE_PRODUCAO_LOCAL+"/{numeroPedido}", producaoResponse.getNumeroPedido());
	}
	
	@Então("a produção é exibida com sucesso")
	public void a_produção_é_exibida_com_sucesso() {
		response.then()
	        .statusCode(HttpStatus.OK.value())
	        .body(matchesJsonSchemaInClasspath("./schemas/ProducaoResponseSchema.json"));
	}
	
	// - ###################################################
	// - Listar produção por situação
	// - ###################################################
	@Quando("requisitar a lista de produção por uma situação")
	public void requisitar_a_lista_de_produção_por_uma_situação() {
		response = given()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
					.get(ControllerMappingConfig.ENDPOINT_MICRO_SERVICE_PRODUCAO_LOCAL + "/situacao/{situacao}",
						 SituacaoProducaoEnum.RECEBIDO.getValue()
						 );
	}
	
	@Então("as produções serão exibidas com sucesso")
	public void as_produções_serão_exibidas_com_sucesso() {
		response.then()
	        .statusCode(HttpStatus.OK.value())
	        .body(matchesJsonSchemaInClasspath("./schemas/ListaProducaoResponseSchema.json"));
	}
	
	// - ###################################################
	// - Atualizar produção
	// - ###################################################
	@Quando("requisitar a alteração do status da produção")
	public void requisitar_a_alteração_do_status_da_produção() {
		var producaoRequest = new ProducaoRequest(
				producaoResponse.getNumeroPedido(),
				producaoResponse.getObservacao(), 
				SituacaoProducaoEnum.EM_PREPARACAO.getValue());		
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(producaoRequest)
				.when()
				.put(ControllerMappingConfig.ENDPOINT_MICRO_SERVICE_PRODUCAO_LOCAL);
	}
	
	@Então("a produção terá seu status atualizado")
	public void a_produção_terá_seu_status_atualizado() {
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body(matchesJsonSchemaInClasspath("./schemas/ProducaoResponseSchema.json"));
	}
}
