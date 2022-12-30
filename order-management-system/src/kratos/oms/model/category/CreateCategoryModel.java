package kratos.oms.model.category;

import kratos.oms.seedwork.Length;
import kratos.oms.seedwork.NotBlank;

public class CreateCategoryModel {
    @NotBlank
    @Length(max = 100, message = "Given name must have valid length between 1 and 100 characters.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
