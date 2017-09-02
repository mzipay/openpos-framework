package org.jumpmind.jumppos.domain.item;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.jumpmind.jumppos.domain.BaseEntity;

@Entity
public class RestrictionValidationQuestion extends BaseEntity {

    @Id
    private String id;

    private String questionText;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }
}
