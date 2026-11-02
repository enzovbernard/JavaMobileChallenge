package br.com.fiap.clyvo_java.repository.pet.saude;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvo_java.model.pet.saude.DoencaAnimal;

public interface DoencaAnimalRepository extends JpaRepository<DoencaAnimal, Long> {

	@Query("SELECT da FROM DoencaAnimal da WHERE da.animal.id_animal = :idAnimal ORDER BY da.dt_diagnostico DESC")
	List<DoencaAnimal> buscarPorAnimal(@Param("idAnimal") Long idAnimal);

}
