package kratos.oms.seedwork;

public class ActionOption<T> extends InputOption {
    private T action;

    public ActionOption(String label, T action) {
        super(label);
        this.action = action;
    }

    public T getAction() {
        return action;
    }
}
