package kratos.oms.seedwork;

import kratos.oms.model.ProductModel;

public class InputOption<T> {
    private String label;
    private T action;

    public InputOption(String label, T action) {
        this.label = label;
        this.action = action;
    }


    public String getLabel() {
        return label;
    }

    public T getAction() {
        return action;
    }
}
