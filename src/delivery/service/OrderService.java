package delivery.service;

import delivery.model.Client;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderItem;
import delivery.model.OrderStatus;
import delivery.shipping.DeliveryMethod;
import delivery.shipping.ExpressDelivery;
import delivery.shipping.Pickup;
import delivery.shipping.StandardDelivery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранит заказы и курьеров в памяти и выполняет операции над ними.
 * С консолью этот класс никак не связан.
 */
public class OrderService {
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private final List<Courier> couriers = new ArrayList<>();
    private final List<DeliveryMethod> deliveryMethods = List.of(
            new StandardDelivery(),
            new ExpressDelivery(),
            new Pickup()
    );
    private int nextOrderId = 1;

    public Courier addCourier(String name, String phone) {
        Courier courier = new Courier(couriers.size() + 1, name, phone);
        couriers.add(courier);
        return courier;
    }

    public Order createOrder(Client client, String address) {
        Order order = new Order(nextOrderId, client, address);
        orders.put(order.getId(), order);
        nextOrderId++;
        return order;
    }

    /**
     * Ищет заказ по номеру.
     *
     * @throws IllegalArgumentException если такого заказа нет
     */
    public Order getOrder(int id) {
        Order order = orders.get(id);
        if (order == null) {
            throw new IllegalArgumentException("Заказ №" + id + " не найден");
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public boolean hasOrders() {
        return !orders.isEmpty();
    }

    public void addItem(int orderId, String productName, int quantity, double price) {
        getOrder(orderId).addItem(new OrderItem(productName, quantity, price));
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethods;
    }

    public void chooseDeliveryMethod(int orderId, DeliveryMethod method) {
        getOrder(orderId).setDeliveryMethod(method);
    }

    public List<Courier> getFreeCouriers() {
        List<Courier> result = new ArrayList<>();
        for (Courier courier : couriers) {
            if (courier.isFree()) {
                result.add(courier);
            }
        }
        return result;
    }

    public void assignCourier(int orderId, int courierId) {
        Order order = getOrder(orderId);
        Courier courier = null;
        for (Courier c : couriers) {
            if (c.getId() == courierId) {
                courier = c;
                break;
            }
        }
        if (courier == null) {
            throw new IllegalArgumentException("Курьер с номером " + courierId + " не найден");
        }
        order.assignCourier(courier);
    }

    public void changeStatus(int orderId, OrderStatus status) {
        getOrder(orderId).changeStatus(status);
    }

    /**
     * Возвращает статусы, в которые заказ можно перевести из текущего.
     */
    public List<OrderStatus> getNextStatuses(int orderId) {
        OrderStatus current = getOrder(orderId).getStatus();
        List<OrderStatus> result = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            if (current.canChangeTo(status)) {
                result.add(status);
            }
        }
        return result;
    }
}
