package delivery.model;

import delivery.shipping.DeliveryMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Заказ. Все изменения идут через методы, которые проверяют,
 * можно ли это сделать в текущем состоянии заказа.
 */
public class Order {
    private final int id;
    private final Client client;
    private final String address;
    private final List<OrderItem> items = new ArrayList<>();
    private DeliveryMethod deliveryMethod;
    private Courier courier;
    private OrderStatus status = OrderStatus.NEW;

    public Order(int id, Client client, String address) {
        if (client == null) {
            throw new IllegalArgumentException("У заказа должен быть клиент");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Адрес доставки не может быть пустым");
        }
        this.id = id;
        this.client = client;
        this.address = address.trim();
    }

    /**
     * Добавляет позицию. Менять состав можно только пока заказ новый.
     */
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Позиция не может быть пустой");
        }
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Товары можно добавлять только в новый заказ");
        }
        items.add(item);
    }

    /**
     * Устанавливает способ доставки. Если новому способу курьер не нужен,
     * ранее назначенный курьер освобождается.
     */
    public void setDeliveryMethod(DeliveryMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Не выбран способ доставки");
        }
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Способ доставки можно менять только у нового заказа");
        }
        deliveryMethod = method;
        if (!method.needsCourier() && courier != null) {
            courier.release();
            courier = null;
        }
    }

    /**
     * Назначает курьера. Если до этого был другой курьер, он освобождается.
     */
    public void assignCourier(Courier newCourier) {
        if (newCourier == null) {
            throw new IllegalArgumentException("Курьер не выбран");
        }
        if (deliveryMethod == null) {
            throw new IllegalStateException("Сначала нужно выбрать способ доставки");
        }
        if (!deliveryMethod.needsCourier()) {
            throw new IllegalStateException("Для способа \"" + deliveryMethod.getName() + "\" курьер не нужен");
        }
        if (status != OrderStatus.NEW && status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Курьера можно назначить только до отправки заказа");
        }
        if (newCourier == courier) {
            throw new IllegalStateException("Этот курьер уже назначен на заказ");
        }
        newCourier.takeOrder();
        if (courier != null) {
            courier.release();
        }
        courier = newCourier;
    }

    /**
     * Переводит заказ в новый статус, если переход разрешён.
     *
     * @throws IllegalStateException если переход недопустим или заказ к нему не готов
     */
    public void changeStatus(OrderStatus newStatus) {
        if (!status.canChangeTo(newStatus)) {
            throw new IllegalStateException("Нельзя перевести заказ из статуса \""
                    + status + "\" в \"" + newStatus + "\"");
        }
        if (newStatus == OrderStatus.CONFIRMED) {
            if (items.isEmpty()) {
                throw new IllegalStateException("В заказе нет ни одного товара");
            }
            if (deliveryMethod == null) {
                throw new IllegalStateException("Не выбран способ доставки");
            }
        }
        if (newStatus == OrderStatus.SHIPPED && needsCourier() && courier == null) {
            throw new IllegalStateException("Перед отправкой нужно назначить курьера");
        }

        status = newStatus;

        if ((status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) && courier != null) {
            courier.release();
        }
    }

    public boolean needsCourier() {
        return deliveryMethod != null && deliveryMethod.needsCourier();
    }

    public double getItemsCost() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getTotal();
        }
        return sum;
    }

    public double getDeliveryCost() {
        if (deliveryMethod == null) {
            return 0;
        }
        return deliveryMethod.calculateCost(getItemsCost());
    }

    public double getTotalCost() {
        return getItemsCost() + getDeliveryCost();
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public Courier getCourier() {
        return courier;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
