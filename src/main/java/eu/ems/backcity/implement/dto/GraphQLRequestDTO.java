package eu.ems.backcity.implement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphQLRequestDTO {
    private String query;
    private String operationName;
    private Map<String, Object> variables;
}
