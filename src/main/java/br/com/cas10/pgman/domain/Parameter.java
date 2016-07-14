package br.com.cas10.pgman.domain;

import java.io.Serializable;

/**
 * @author kasten
 * @since 14/07/16
 */
public class Parameter implements Serializable {

    private String name;
    private String value;
    private String defaultValue;
    private String description;
    private String source;

    public Parameter(String name, String value, String defaultValue, String description, String source) {
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.description = description;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
