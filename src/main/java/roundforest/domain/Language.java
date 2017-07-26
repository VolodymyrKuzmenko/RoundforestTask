package roundforest.domain;

import lombok.AllArgsConstructor;
import org.codehaus.jackson.annotate.JsonValue;

@AllArgsConstructor
public enum Language {
    EN("en"), FR("fr");

    private String code;

    @JsonValue
    public String toValue() {
        return this.code;
    }
}
