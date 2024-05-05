package br.com.postech.techchallenge.microservico.producao.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.com.postech.techchallenge.microservico.producao.enums.SituacaoProducaoEnum;
import br.com.postech.techchallenge.microservico.producao.util.Constantes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Producao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "pedido_id")
	private Long numeroPedido;
	
	@Column(name = "observacao")
	private String observacao;
	
	@Type(value = br.com.postech.techchallenge.microservico.producao.enums.AssociacaoType.class, parameters = {
			@Parameter(name = Constantes.ENUM_CLASS_NAME, value = "SituacaoProducaoEnum") })
	@Column(name = "situacao_producao_id")
	private SituacaoProducaoEnum situacaoProducao;
	
    @Column(name = "data_inicio_preparo", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataInicioPreparo;

    @Column(name = "data_fim_preparo", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataFimPreparo;
}
