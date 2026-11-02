package br.com.fiap.clyvo_java.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.clyvo_java.model.usuario.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {}