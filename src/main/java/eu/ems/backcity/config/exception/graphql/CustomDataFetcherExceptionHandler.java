package eu.ems.backcity.config.exception.graphql;

import eu.ems.backcity.config.exception.DefautBusinessException;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.AccessDeniedException;
import java.util.concurrent.CompletableFuture;

public class CustomDataFetcherExceptionHandler implements DataFetcherExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomDataFetcherExceptionHandler.class);


    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        var exception = handlerParameters.getException();
        var sourceLocation = handlerParameters.getSourceLocation();
        var path = handlerParameters.getPath();

        var error = getGraphQLError(exception, sourceLocation, path);

        logaErro(exception, error);

        return CompletableFuture.completedFuture(DataFetcherExceptionHandlerResult.newResult().error(error).build());
    }

    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        var exception = handlerParameters.getException();
        var sourceLocation = handlerParameters.getSourceLocation();
        var path = handlerParameters.getPath();

        var error = getGraphQLError(exception, sourceLocation, path);

        logaErro(exception, error);

        return DataFetcherExceptionHandlerResult.newResult().error(error).build();
    }

    private void logaErro(Throwable exception, GraphQLError error) {
        if (exception instanceof AccessDeniedException) {
            log.warn(error.getMessage());
        } else if (exception instanceof DefautBusinessException) {
            var primeiraLinha = exception.getStackTrace()[0];
            var posicaoErro = "[" + primeiraLinha.getFileName() + ":" + primeiraLinha.getLineNumber() + "] ";
            log.error(posicaoErro + error.getMessage());
        } else {
            log.error(error.getMessage(), exception);
        }
    }

    private GraphQLError getGraphQLError(Throwable exception, SourceLocation sourceLocation, ResultPath path) {
        GraphQLError error;
        if (exception instanceof DefautBusinessException) {
            error = new DefautBusinessException(path, exception.getMessage(), sourceLocation);
        } else {
            error = new ExceptionWhileDataFetching(path, exception, sourceLocation);
        }
        return error;
    }
}
