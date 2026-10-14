package delivery.console;

import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderItem;
import delivery.model.OrderStatus;
import delivery.shipping.DeliveryMethod;

import java.util.List;

/**
 * Вывод информации о заказах, курьерах и способах доставки.
 */
public class OrderPrinter {

    public void printOrderList(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }
        for (Order order : orders) {
            String delivery = order.getDeliveryMethod() == null
                    ? "доставка не выбрана"
                    : order.getDeliveryMethod().getName();
            System.out.printf("№%d | %s | %s | %s | %s%n",
                    order.getId(),
                    order.getClient().getName(),
                    order.getStatus(),
                    delivery,
                    money(order.getTotalCost()));
        }
    }

    public void printOrder(Order order) {
        System.out.println("----- Заказ №" + order.getId() + " -----");
        System.out.println("Клиент: " + order.getClient());
        System.out.println("Адрес: " + order.getAddress());
        System.out.println("Статус: " + order.getStatus());
        System.out.println("Товары:");
        for (OrderItem item : order.getItems()) {
            System.out.printf("  %s x%d по %s = %s%n",
                    item.getProductName(),
                    item.getQuantity(),
                    money(item.getPrice()),
                    money(item.getTotal()));
        }
        System.out.println("Сумма товаров: " + money(order.getItemsCost()));

        DeliveryMethod method = order.getDeliveryMethod();
        if (method == null) {
            System.out.println("Способ доставки: не выбран");
        } else {
            System.out.println("Способ доставки: " + method.getName()
                    + ", " + money(order.getDeliveryCost())
                    + ", примерно " + days(method.getDeliveryDays()));
            if (method.needsCourier()) {
                Courier courier = order.getCourier();
                System.out.println("Курьер: " + (courier == null ? "не назначен" : courier));
            }
        }
        System.out.println("Итого: " + money(order.getTotalCost()));
    }

    /**
     * Показывает все способы доставки с ценой, посчитанной для конкретного заказа.
     */
    public void printDeliveryMethods(List<DeliveryMethod> methods, Order order) {
        for (int i = 0; i < methods.size(); i++) {
            DeliveryMethod method = methods.get(i);
            System.out.printf("%d. %s - %s, %s%n",
                    i + 1,
                    method.getName(),
                    money(method.calculateCost(order.getItemsCost())),
                    days(method.getDeliveryDays()));
        }
    }

    public void printDeliverySummary(Order order) {
        DeliveryMethod method = order.getDeliveryMethod();
        System.out.println("Выбрано: " + method.getName());
        System.out.println("Стоимость доставки: " + money(order.getDeliveryCost())
                + ", срок: " + days(method.getDeliveryDays()));
        System.out.println("Итого к оплате: " + money(order.getTotalCost()));
    }

    public void printCouriers(List<Courier> couriers) {
        for (Courier courier : couriers) {
            System.out.println(courier.getId() + ". " + courier);
        }
    }

    public void printStatuses(List<OrderStatus> statuses) {
        for (int i = 0; i < statuses.size(); i++) {
            System.out.println((i + 1) + ". " + statuses.get(i));
        }
    }

    private String money(double value) {
        return String.format("%.2f руб.", value);
    }

    private String days(int count) {
        if (count == 1) {
            return "1 день";
        }
        if (count >= 2 && count <= 4) {
            return count + " дня";
        }
        return count + " дней";
    }
}
