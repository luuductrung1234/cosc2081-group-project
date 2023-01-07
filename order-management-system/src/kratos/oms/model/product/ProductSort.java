package kratos.oms.model.product;

public enum ProductSort {
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
    },
    PriceAscending {
        @Override
        public String toString() {
            return "Price (ascending)";
        }
    },
    PriceDescending {
        @Override
        public String toString() {
            return "Price (descending)";
        }
    }
}
