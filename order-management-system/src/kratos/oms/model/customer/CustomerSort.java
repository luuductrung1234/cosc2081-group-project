package kratos.oms.model.customer;

public enum CustomerSort {
    NameAscending {
        @Override
        public String toString() {
            return "Name (ascending)";
        }
    },
    NameDescending {
        @Override
        public String toString() {
            return "Name (descending)";
        }
    }
}
