package eu.ems.backcity.implement.graphql;

import eu.ems.backcity.app.data.entity.Usuario;
import eu.ems.backcity.config.annotation.GraphQLService;
import eu.ems.backcity.implement.service.UsuarioService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GraphQLService
public class UsuarioGraphQLService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioGraphQLService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GraphQLQuery
    public Usuario findByIdUsuario(@GraphQLNonNull @GraphQLArgument(name = "id") Long id) {
        log.info("Localizando pelo id [{}]", id);

        return usuarioService.findById(id);
    }

    @GraphQLMutation
    public Usuario UsuariarioCreate(@GraphQLNonNull @GraphQLArgument(name = "usuario") Usuario usuario) {
        return usuarioService.register(usuario);
    }
}

