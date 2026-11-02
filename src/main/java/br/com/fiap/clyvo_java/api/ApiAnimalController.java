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
import br.com.fiap.clyvo_java.model.pet.Animal;
import br.com.fiap.clyvo_java.model.individuos.Responsavel;
import br.com.fiap.clyvo_java.repository.pet.AnimalRepository;
import br.com.fiap.clyvo_java.repository.individuos.ResponsavelRepository;

@RestController
@RequestMapping("/api/animais")
public class ApiAnimalController {
    private final AnimalRepository animais;
    private final ResponsavelRepository responsaveis;

    public ApiAnimalController(AnimalRepository animais, ResponsavelRepository responsaveis) {
        this.animais = animais;
        this.responsaveis = responsaveis;
    }

    public record Entrada(
        @NotBlank @Size(max=50) String nome,
        Integer idade,
        @NotBlank @Size(max=50) String especie,
        String raca,
        @NotBlank @Pattern(regexp="(?i)[MF]") String sexo,
        @NotNull @PastOrPresent LocalDate dataNascimento,
        Double peso,
        @NotNull @Positive Long responsavelId) {}

    public record Saida(Long id, String nome, Integer idade, String especie, String raca,
                        String sexo, LocalDate dataNascimento, Double peso,
                        Long responsavelId, String responsavelNome) {}

    private static Saida dto(Animal a) {
        Responsavel r = a.getResponsavel();
        return new Saida(a.getId_animal(), a.getNm_animal(), a.getIdade(), a.getEspecie(),
                a.getRaca(), a.getSexo(), a.getDt_nascimento(), a.getPeso(),
                r == null ? null : r.getId_responsavel(),
                r == null ? "" : r.getNm_responsavel());
    }

    private void aplicar(Animal a, Entrada d) {
        Responsavel r = responsaveis.findById(d.responsavelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tutor inexistente"));
        a.setNm_animal(d.nome().trim());
        a.setIdade(d.idade());
        a.setEspecie(d.especie().trim());
        a.setRaca(d.raca() == null || d.raca().isBlank() ? null : d.raca().trim());
        a.setSexo(d.sexo().trim().toUpperCase());
        a.setDt_nascimento(d.dataNascimento());
        a.setPeso(d.peso());
        a.setResponsavel(r);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Saida> listar() {
        return animais.findAll().stream().map(ApiAnimalController::dto).toList();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Saida> criar(@Valid @RequestBody Entrada d) {
        Animal a = new Animal();
        aplicar(a,d);
        a = animais.save(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto(a));
    }

    @PutMapping("/{id}")
    @Transactional
    public Saida atualizar(@PathVariable Long id, @Valid @RequestBody Entrada d) {
        Animal a = animais.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        aplicar(a,d);
        return dto(animais.save(a));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Animal a = animais.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        animais.delete(a);
        return ResponseEntity.noContent().build();
    }
}