package kratos.oms.model.customer;

public class SearchCustomerModel {
    private String searchText;
    private CustomerSort sortedBy;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public CustomerSort getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(CustomerSort sortedBy) {
        this.sortedBy = sortedBy;
    }
}
