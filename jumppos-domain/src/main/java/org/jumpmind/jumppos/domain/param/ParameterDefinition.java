package org.jumpmind.jumppos.domain.param;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParameterDefinition {

    @Id
    private ParameterDefinitionId id;
    private String description;
    private String type;

    public ParameterDefinition() {
        super();
    }

    public ParameterDefinition(ParameterDefinitionId id) {
        super();
        this.id = id;
    }

    public ParameterDefinition(String category, String subcategory, String name, String type, String description) {
        super();
        this.id = new ParameterDefinitionId(category, subcategory, name);
        this.description = description;
        this.type = type;
    }

    public ParameterDefinitionId getId() {
        return id;
    }

    public void setId(ParameterDefinitionId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
