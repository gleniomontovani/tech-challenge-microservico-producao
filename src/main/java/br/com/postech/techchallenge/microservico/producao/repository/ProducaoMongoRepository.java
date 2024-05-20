package br.com.postech.techchallenge.microservico.producao.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.postech.techchallenge.microservico.producao.domain.ProducaoDocumento;

@Repository
public interface ProducaoMongoRepository extends MongoRepository<ProducaoDocumento, Long>{

	@Query("{ 'numeroPedido' : ?0 }")
	Optional<ProducaoDocumento> findByNumeroPedido(Long numeroPedido);
}
