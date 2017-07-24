package roundforest;

/**
 * Created by Vova on 7/25/2017.
 */
public enum ReviewFields {
    PROFILE_NAME("ProfileName"),
    PRODUCT_ID("ProductId");


    private String name;

    ReviewFields(String fildName) {
        this.name = fildName;
    }

    @Override
    public String toString() {
        return name;
    }
}
