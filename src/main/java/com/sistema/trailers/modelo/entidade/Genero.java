package com.sistema.trailers.modelo.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Genero {

	@Id
	@Column(name = "id_genero")
	private Integer idGenero;
	
	private String titulo;
	
}
