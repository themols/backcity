package eu.ems.backcity.api;

import eu.ems.backcity.app.data.entity.Usuario;
import eu.ems.backcity.app.enums.TipoUsuario;
import eu.ems.backcity.implement.dto.AuthenticateRequest;
import eu.ems.backcity.implement.service.UsuarioService;
import eu.ems.backcity.implement.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil generate;

    @Autowired
    public AuthController(UsuarioService usuarioService, JwtUtil generate) {
        this.usuarioService = usuarioService;
        this.generate = generate;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login( @RequestBody @Valid AuthenticateRequest request) {
        var usuario = usuarioService.login(request.getUsername(), request.getPassword());

        if (usuario.isPresent()) {
            String token = generate.token(usuario.get().getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout efetuado (client-side).");
    }
}
