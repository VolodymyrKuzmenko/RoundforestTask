package roundforest.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@NoArgsConstructor
@Data
@JsonPropertyOrder({"input_lang", "output_lang", "text"})
public class ReviewDTO {
    @JsonProperty("input_lang")
    private Languages inputLanguage;
    @JsonProperty("output_lang")
    private Languages outputLanguages;
    @JsonProperty
    private String text;
}
