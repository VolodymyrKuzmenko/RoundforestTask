package roundforest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({"input_lang", "output_lang", "text"})
public class ReviewDTO {
    @JsonProperty
    private String text;
    @JsonProperty("input_lang")
    private Language inputLanguage;
    @JsonProperty("output_lang")
    private Language outputLanguages;
}
