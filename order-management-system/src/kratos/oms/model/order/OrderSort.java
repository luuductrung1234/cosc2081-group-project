package kratos.oms.model.order;

public enum OrderSort {
    DateAscending {
        @Override
        public String toString() {
            return "Date (ascending)";
        }
    },
    DateDescending {
        @Override
        public String toString() {
            return "Date (descending)";
        }
    },
    AmountAscending {
        @Override
        public String toString() {
            return "Amount (ascending)";
        }
    },
    AmountDescending {
        @Override
        public String toString() {
            return "Amount (descending)";
        }
    }
}
