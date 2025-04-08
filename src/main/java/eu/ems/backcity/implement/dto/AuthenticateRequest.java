package eu.ems.backcity.implement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateRequest {

    @NotEmpty(message = "o username é obrigatorio")
    @NotBlank(message = "o username é obrigatorio")
    private String username;

    @Size(min = 8, message = "password precisa ter no minimo 8 caracteres")
    @NotEmpty(message = "o password é obrigatorio")
    @NotBlank(message = "o password é obrigatorio")
    private String password;
}
