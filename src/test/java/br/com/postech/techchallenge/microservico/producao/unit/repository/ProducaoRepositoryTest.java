package br.com.postech.techchallenge.microservico.producao.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.repository.ProducaoJpaRepository;
import br.com.postech.techchallenge.microservico.producao.util.ObjectCreatorHelper;

class ProducaoRepositoryTest {
	
	@Mock
	private ProducaoJpaRepository producaoJpaRepository;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
	}
	
	@AfterEach	
	void close() throws Exception {
		openMocks.close();
	}

	@Test
	void devePermitirBucarProducaoPorSituacao() {
		var producaoModel1 = ObjectCreatorHelper.obterProducao();
		producaoModel1.setId(1L);
		
		var producaoModel2 = ObjectCreatorHelper.obterProducao();
		producaoModel2.setId(2L);
		
		var producoes = Arrays.asList(producaoModel1, producaoModel2);
		
		when(producaoJpaRepository.findBySituacaoProducao(any())).thenReturn(producoes);
		
		var lista = producaoJpaRepository.findBySituacaoProducao(SituacaoProducaoEnum.RECEBIDO);
		
		assertThat(lista).hasSize(2).containsExactlyInAnyOrder(producaoModel1, producaoModel2);
		
		verify(producaoJpaRepository, times(1)).findBySituacaoProducao(any());		
	}

	@Test
	void devePermitirBuscarProducaoPorNumeroPedido() {
		var producao = ObjectCreatorHelper.obterProducao();
		producao.setId(1L);
		
		when(producaoJpaRepository.findByNumeroPedido(anyLong())).thenReturn(Optional.of(producao));
		
		var producaoOptional = producaoJpaRepository.findByNumeroPedido(1L);
		
		assertThat(producaoOptional).isPresent().containsSame(producao);
		
		producaoOptional.ifPresent(producaoEncontrado -> {
			assertThat(producaoEncontrado).isNotNull().isInstanceOf(Producao.class);
			assertThat(producaoEncontrado.getId()).isEqualTo(producao.getId());
			assertThat(producaoEncontrado.getNumeroPedido()).isEqualTo(producao.getNumeroPedido());
			assertThat(producaoEncontrado.getDataInicioPreparo()).isNotNull();
			assertThat(producaoEncontrado.getObservacao()).isNotNull();
			assertThat(producaoEncontrado.getSituacaoProducao()).isEqualTo(producao.getSituacaoProducao());
		});
				
		verify(producaoJpaRepository, times(1)).findByNumeroPedido(anyLong());
	}
	
	@Test
	void devePermitirBuscarProducaoPorId() {
		var producao = ObjectCreatorHelper.obterProducao();
		producao.setId(1L);
		
		when(producaoJpaRepository.findById(anyLong())).thenReturn(Optional.of(producao));
		
		var producaoOptional = producaoJpaRepository.findById(1L);
		
		assertThat(producaoOptional).isPresent().containsSame(producao);
		
		producaoOptional.ifPresent(producaoEncontrado -> {
			assertThat(producaoEncontrado).isNotNull().isInstanceOf(Producao.class);
			assertThat(producaoEncontrado.getId()).isEqualTo(producao.getId());
			assertThat(producaoEncontrado.getNumeroPedido()).isEqualTo(producao.getNumeroPedido());
			assertThat(producaoEncontrado.getDataInicioPreparo()).isNotNull();
			assertThat(producaoEncontrado.getObservacao()).isNotNull();
			assertThat(producaoEncontrado.getSituacaoProducao()).isEqualTo(producao.getSituacaoProducao());
		});
				
		verify(producaoJpaRepository, times(1)).findById(anyLong());
	}

	@Test
	void devePermitirSalvarProducao() {
		var producao = ObjectCreatorHelper.obterProducao();
		producao.setId(1L);
		
		when(producaoJpaRepository.save(any())).thenReturn(producao);
		
		var producaoSalvo = producaoJpaRepository.save(producao);
		
		assertThat(producaoSalvo).isInstanceOf(Producao.class).isNotNull().isEqualTo(producao);
		assertThat(producaoSalvo).extracting(Producao::getId).isEqualTo(producao.getId());
		assertThat(producaoSalvo).extracting(Producao::getNumeroPedido).isEqualTo(producao.getNumeroPedido());
		assertThat(producaoSalvo).extracting(Producao::getDataInicioPreparo).isNotNull();
		assertThat(producaoSalvo).extracting(Producao::getObservacao).isNotNull();
		assertThat(producaoSalvo).extracting(Producao::getSituacaoProducao).isEqualTo(producao.getSituacaoProducao());
		
		verify(producaoJpaRepository, times(1)).save(any());
	}
}
