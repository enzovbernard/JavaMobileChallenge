package br.com.fiap.clyvo_java.repository.pet.consultas;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.clyvo_java.model.pet.consultas.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	@Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.veterinario.id_veterinario = :idVeterinario AND c.dt_consulta = :dtConsulta")
	boolean existeConsultaMesmoVeterinarioMesmoDia(@Param("idVeterinario") Long idVeterinario, @Param("dtConsulta") LocalDate dtConsulta);

	@Query("SELECT c FROM Consulta c WHERE c.dt_consulta BETWEEN :inicio AND :fim ORDER BY c.dt_consulta")
	List<Consulta> buscarConsultasPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

	@Query("SELECT c FROM Consulta c WHERE c.animal.id_animal = :idAnimal ORDER BY c.dt_consulta DESC")
	List<Consulta> buscarPorAnimal(@Param("idAnimal") Long idAnimal);

	@Query("SELECT c FROM Consulta c WHERE c.dt_consulta >= :hoje ORDER BY c.dt_consulta")
	List<Consulta> buscarAPartirDe(@Param("hoje") LocalDate hoje);

}