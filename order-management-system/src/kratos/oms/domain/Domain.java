package kratos.oms.domain;

public abstract class Domain<TId> {
    protected TId id;

    public Domain(TId id) {
        this.id = id;
    }

    /**
     * Convert instance of this class into string (serialization)
     *
     * @return a string contains data of Domain serialized instance
     */
    public String serialize() {
        return String.format("%s", id);
    }

    /**
     * This static method will be overrided by subclass(es)
     *
     * @param data serialized string data
     * @return new instance of Account
     */
    public static <TId> Domain<TId> deserialize(String data) {
        throw new UnsupportedOperationException("fromString() has not been implemented!");
    }
}
