package br.com.fiap.clyvo_java.api;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import br.com.fiap.clyvo_java.model.usuario.Pessoa;
import br.com.fiap.clyvo_java.model.usuario.Usuario;
import br.com.fiap.clyvo_java.repository.usuario.PessoaRepository;
import br.com.fiap.clyvo_java.repository.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    private final UsuarioRepository usuarios;
    private final PessoaRepository pessoas;
    private final PasswordEncoder passwordEncoder;

    public ApiAuthController(UsuarioRepository usuarios, PessoaRepository pessoas, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.pessoas = pessoas;
        this.passwordEncoder = passwordEncoder;
    }

    public record Cadastro(
        @NotBlank @Size(max=60) String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(max=50) String username,
        @NotBlank @Size(min=4, max=72) String password) {}

    public record UsuarioResposta(Long id, String username, String role, String nome, String email) {}

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UsuarioResposta me(Authentication auth) {
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Usuario u = usuarios.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return resposta(u);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UsuarioResposta> cadastrar(@Valid @RequestBody Cadastro dados) {
        String username = dados.username().trim();
        if (usuarios.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja cadastrado");
        }
        Pessoa p = new Pessoa();
        p.setNome(dados.nome().trim());
        p.setEmailPessoal(dados.email().trim());
        p = pessoas.save(p);

        Usuario u = new Usuario();
        u.setPessoa(p);
        u.setUsername(username);
        u.setSenha(passwordEncoder.encode(dados.password()));
        u.setPermissao("USER");
        u.setDataCriacao(LocalDate.now());
        u = usuarios.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta(u));
    }

    private static UsuarioResposta resposta(Usuario u) {
        Pessoa p = u.getPessoa();
        return new UsuarioResposta(u.getId(), u.getUsername(), u.getPermissao(),
                p == null ? "" : p.getNome(), p == null ? "" : p.getEmailPessoal());
    }
}