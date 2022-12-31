package kratos.oms.seedwork;

public class ValueOption<T> extends InputOption {
    private T value;

    public ValueOption(String label, T value) {
        super(label);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
