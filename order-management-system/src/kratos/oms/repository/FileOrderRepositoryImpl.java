package kratos.oms.repository;

import kratos.oms.domain.Order;
import kratos.oms.domain.OrderItem;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileOrderRepositoryImpl extends BaseFileRepository implements OrderRepository {
    private final static String DATA_FILE_NAME = "orders.txt";
    private final static String DATA_ITEM_FILE_NAME = "orderItems.txt";

    public FileOrderRepositoryImpl(String directoryUrl) {
        super(directoryUrl);
    }

    @Override
    public List<Order> listAll() {
        try {
            List<Order> orders = this.read(DATA_FILE_NAME, Order.class);
            List<OrderItem> items = this.read(DATA_ITEM_FILE_NAME, OrderItem.class);
            for (Order order : orders) {
                List<OrderItem> orderItems = items.stream()
                        .filter(i -> i.getOrderId().equals(order.getId()))
                        .collect(Collectors.toList());
                order.addItems(orderItems);
            }
            return orders;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return listAll().stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    @Override
    public boolean add(Order order) {
        List<Order> orders = listAll();
        Optional<Order> existingOrder = orders.stream()
                .filter(o -> o.getId().equals(order.getId())).findFirst();
        if (existingOrder.isPresent())
            return false;
        orders.add(order);
        List<OrderItem> orderItems = orders.stream()
                .flatMap(o -> o.getItems().stream()).collect(Collectors.toList());
        try {
            this.write(DATA_FILE_NAME, orders);
            this.write(DATA_ITEM_FILE_NAME, orderItems);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "add", e);
            return false;
        }
    }

    @Override
    public boolean update(Order order) {
        List<Order> orders = listAll();
        Optional<Order> existingOrder = orders.stream()
                .filter(o -> o.getId().equals(order.getId())).findFirst();
        if (existingOrder.isEmpty())
            return false;
        orders.removeIf(o -> o.getId().equals(order.getId()));
        orders.add(order);
        return writeOrders(orders);
    }

    @Override
    public boolean delete(UUID id) {
        List<Order> orders = listAll();
        Optional<Order> existingOrder = orders.stream()
                .filter(o -> o.getId().equals(id)).findFirst();
        if (existingOrder.isEmpty())
            return false;
        orders.removeIf(o -> o.getId().equals(id));
        return writeOrders(orders);
    }

    private boolean writeOrders(List<Order> orders) {
        List<OrderItem> orderItems = orders.stream()
                .flatMap(c -> c.getItems().stream()).collect(Collectors.toList());
        try {
            this.write(DATA_FILE_NAME, orders);
            this.write(DATA_ITEM_FILE_NAME, orderItems);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "writeOrders", e);
            return false;
        }
    }
}
