package kratos.oms.repository;

import kratos.oms.domain.Cart;
import kratos.oms.domain.Order;
import kratos.oms.domain.OrderStatus;
import kratos.oms.domain.Product;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileOrderRepository extends BaseFileRepository implements OrderRepository{
    public FileOrderRepository(String fileUrl) {
        super(fileUrl);
    }

    @Override
    public List<Order> listAll(UUID accountId, OrderStatus status, String sortedBy) {
        try {
            ArrayList<Order> orders=new ArrayList<>(this.read(Order.class));

            orders.stream()
                    .filter(a -> a.getAccountId().equals(accountId))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            orders.stream()
                    .filter(a -> a.getStatus().equals(status))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            return orders;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }
    public List<Order> listAll() {
        try {
            return new ArrayList<>(this.read(Order.class));
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }
    @Override
    public Optional<Order> findById(UUID id) {
        return listAll().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public boolean add(Order order) {
        List<Order> orders = listAll();
        orders.add(order);
        try {
            this.write(orders);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean update(Order order) {
        List<Order> orders = listAll();
        orders.removeIf(a -> a.getId().equals(order.getId()));
        orders.add(order);
        try {
            this.write(orders);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }

    @Override
    public boolean delete(Order order) {
        List<Order> orders = listAll();
        orders.removeIf(a -> a.getId().equals(order.getId()));
        try {
            this.write(orders);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }
}
