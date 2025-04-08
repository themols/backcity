package eu.ems.backcity.api;

import eu.ems.backcity.config.annotation.GraphQLService;
import eu.ems.backcity.config.exception.graphql.CustomDataFetcherExceptionHandler;
import eu.ems.backcity.implement.dto.GraphQLRequestDTO;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.SubscriptionExecutionStrategy;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static graphql.ExecutionInput.newExecutionInput;
import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/v1/graphql")
public class GraphQLController {
    private final GraphQL graphQL;

    @Autowired
    public GraphQLController(ApplicationContext context) {
        LoggerFactory.getLogger(this.getClass()).debug("Inicializando endpoint do Graphql");
        this.graphQL = getGraphqlInstance(context.getBeansWithAnnotation(GraphQLService.class).values().toArray());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> search(@RequestBody GraphQLRequestDTO requestBody,
                                      GraphQLRequestDTO requestParams,
                                      HttpServletRequest raw) {

        var variablesParams = ofNullable(requestParams.getVariables());
        var variablesBody = ofNullable(requestBody.getVariables());

        var query = ofNullable(requestParams.getQuery())
                .orElse(requestBody.getQuery());
        var operationName = ofNullable(requestParams.getOperationName())
                .orElse(requestBody.getOperationName());

        var variables = variablesParams.or(() -> variablesBody)
                .orElse(new HashMap<>());

        preencheIpAddress(raw, variables);

        var build = newExecutionInput()
                .query(query)
                .operationName(operationName)
                .variables(variables)
                .context(raw)
                .build();

        ExecutionResult executionResult = graphQL.execute(
                build
        );

        return executionResult.toSpecification();
    }

    public GraphQL getGraphqlInstance(Object... graphQLServices) {
        var schema = new GraphQLSchemaGenerator()
                .withResolverBuilders(new AnnotatedResolverBuilder())
                .withOperationsFromSingletons(graphQLServices)
                .withValueMapperFactory(new JacksonValueMapperFactory())

                .generate();

        return GraphQL.newGraphQL(schema)
                .queryExecutionStrategy(new AsyncExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy(new CustomDataFetcherExceptionHandler()))
                .build();
    }

    private void preencheIpAddress(HttpServletRequest raw, Map<String, Object> variables) {
        var remoteAddr = raw.getRemoteAddr();

        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            remoteAddr = "192.168.1.192";
        }

        variables.put("ipAddress", remoteAddr);
    }

}
