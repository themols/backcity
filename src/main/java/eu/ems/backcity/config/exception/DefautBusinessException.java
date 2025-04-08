package eu.ems.backcity.config.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static graphql.Assert.assertNotNull;

public class DefautBusinessException extends RuntimeException implements GraphQLError {

    private List<Object> path = new ArrayList<>();
    private List<SourceLocation> locations = new ArrayList<>();

    public DefautBusinessException(ResultPath path, String message, SourceLocation sourceLocation) {
        this(message);

        this.path = assertNotNull(path).toList();
        this.locations = Collections.singletonList(sourceLocation);
    }

    public DefautBusinessException() {
        super();
    }

    public DefautBusinessException(String message) {
        super(message);
    }

    public DefautBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefautBusinessException(Throwable cause) {
        super(cause);
    }

    protected DefautBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return locations;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    @Override
    public List<Object> getPath() {
        return path;
    }
}

