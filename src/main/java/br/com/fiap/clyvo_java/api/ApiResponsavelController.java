package br.com.fiap.clyvo_java.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import br.com.fiap.clyvo_java.model.individuos.Responsavel;
import br.com.fiap.clyvo_java.repository.individuos.ResponsavelRepository;
import br.com.fiap.clyvo_java.repository.pet.AnimalRepository;

@RestController
@RequestMapping("/api/responsaveis")
public class ApiResponsavelController {
    private final ResponsavelRepository responsaveis;
    private final AnimalRepository animais;

    public ApiResponsavelController(ResponsavelRepository responsaveis, AnimalRepository animais) {
        this.responsaveis = responsaveis;
        this.animais = animais;
    }

    public record Entrada(
        @NotBlank @Size(max=50) String nome,
        String cpf,
        @NotNull @Past LocalDate dataNascimento,
        @NotBlank @Email @Size(max=50) String email,
        @NotBlank @Size(max=20) String celular) {}

    public record Saida(Long id, String nome, String cpf, LocalDate dataNascimento, String email, String celular) {}

    private static Saida dto(Responsavel r) {
        return new Saida(r.getId_responsavel(), r.getNm_responsavel(), r.getCpf(), r.getDt_nascimento(), r.getEmail(), r.getNum_celular());
    }

    private static void aplicar(Responsavel r, Entrada d) {
        r.setNm_responsavel(d.nome().trim());
        r.setCpf(d.cpf() == null || d.cpf().isBlank() ? null : d.cpf().trim());
        r.setDt_nascimento(d.dataNascimento());
        r.setEmail(d.email().trim());
        r.setNum_celular(d.celular().trim());
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Saida> listar() {
        return responsaveis.findAll().stream().map(ApiResponsavelController::dto).toList();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Saida> criar(@Valid @RequestBody Entrada d) {
        Responsavel r = new Responsavel();
        aplicar(r,d);
        r = responsaveis.save(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto(r));
    }

    @PutMapping("/{id}")
    @Transactional
    public Saida atualizar(@PathVariable Long id, @Valid @RequestBody Entrada d) {
        Responsavel r = responsaveis.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        aplicar(r,d);
        return dto(responsaveis.save(r));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Responsavel r = responsaveis.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        boolean possuiPets = animais.findAll().stream()
                .anyMatch(a -> a.getResponsavel() != null && id.equals(a.getResponsavel().getId_responsavel()));
        if (possuiPets) throw new ResponseStatusException(HttpStatus.CONFLICT, "Exclua os pets vinculados primeiro");
        responsaveis.delete(r);
        return ResponseEntity.noContent().build();
    }
}