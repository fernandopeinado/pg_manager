package br.com.cas10.pgman.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kasten
 * @since 14/07/16
 */
public class ParameterCategory implements Serializable {
    private String name;
    private List<Parameter> parameters = new ArrayList<>();

    public ParameterCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
