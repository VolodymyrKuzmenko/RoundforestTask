package roundforest.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReviewFields {
    PROFILE_NAME("ProfileName"),
    PRODUCT_ID("ProductId"),
    SUMMARY("Summary"),
    TEXT("Text");

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
