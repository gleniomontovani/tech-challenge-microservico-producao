package br.com.postech.techchallenge.microservico.producao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallenge.microservico.producao.entity.Producao;
import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;

@Repository
public interface ProducaoJpaRepository extends JpaRepository<Producao, Long>{

	List<Producao> findBySituacaoProducao(SituacaoProducaoEnum situacaoProducao);
	Optional<Producao> findByNumeroPedido(Long numeroPedido);
}
