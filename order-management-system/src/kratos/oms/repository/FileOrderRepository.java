package kratos.oms.repository;

import kratos.oms.domain.Order;
import kratos.oms.domain.OrderStatus;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileOrderRepository extends BaseFileRepository implements OrderRepository {
    private final static String DATA_FILE_NAME = "orders.txt";

    public FileOrderRepository(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public List<Order> listAll(UUID accountId, OrderStatus status, String sortedBy) {
        return new ArrayList<>();
    }

    public List<Order> listAll() {
        try {
            return this.read(DATA_FILE_NAME, Order.class);
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
            this.write(DATA_FILE_NAME, orders);
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
            this.write(DATA_FILE_NAME, orders);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
            return false;
        }
    }

    @Override
    public boolean delete(Order order) {
        List<Order> orders = listAll();
        try {
            if (orders.removeIf(a -> a.getId().equals(order.getId()))) {
                this.write(DATA_FILE_NAME, orders);
                return true;
            }
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "update", e);
        }
        return false;
    }
}
