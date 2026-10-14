package delivery.console;

import delivery.model.Client;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderStatus;
import delivery.service.OrderService;
import delivery.shipping.DeliveryMethod;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Главное меню программы. Читает команды пользователя
 * и передаёт их в {@link OrderService}.
 */
public class ConsoleMenu {
    private final OrderService service;
    private final ConsoleInput input;
    private final OrderPrinter printer;

    public ConsoleMenu(OrderService service, ConsoleInput input, OrderPrinter printer) {
        this.service = service;
        this.input = input;
        this.printer = printer;
    }

    public void run() {
        System.out.println("Служба доставки. Выберите действие из меню.");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = input.readInt("> ", 0, 6);
                System.out.println();
                try {
                    switch (choice) {
                        case 1 -> createOrder();
                        case 2 -> printer.printOrderList(service.getAllOrders());
                        case 3 -> chooseDelivery();
                        case 4 -> assignCourier();
                        case 5 -> changeStatus();
                        case 6 -> showOrder();
                        case 0 -> running = false;
                        default -> System.out.println("Нет такого пункта");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // ввод закрыли (например, Ctrl+D) - просто выходим
            System.out.println();
        }
        System.out.println("Работа завершена.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Создать заказ");
        System.out.println("2. Список заказов");
        System.out.println("3. Выбрать способ доставки");
        System.out.println("4. Назначить курьера");
        System.out.println("5. Изменить статус заказа");
        System.out.println("6. Показать заказ");
        System.out.println("0. Выход");
    }

    private void createOrder() {
        Client client = readClient();
        String address = input.readNotEmpty("Адрес доставки: ");
        Order order = service.createOrder(client, address);

        System.out.println("Добавьте товары. Чтобы закончить, оставьте название пустым.");
        while (true) {
            String name = input.readLine("Товар: ");
            if (name.isEmpty()) {
                if (order.getItems().isEmpty()) {
                    System.out.println("Нужно добавить хотя бы один товар.");
                    continue;
                }
                break;
            }
            int quantity = input.readPositiveInt("Количество: ");
            double price = input.readPositiveDouble("Цена за штуку: ");
            service.addItem(order.getId(), name, quantity, price);
        }

        System.out.println("Создан заказ №" + order.getId());
        printer.printOrder(order);
    }

    private Client readClient() {
        while (true) {
            String name = input.readNotEmpty("Имя клиента: ");
            String phone = input.readNotEmpty("Телефон: ");
            try {
                return new Client(name, phone);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void chooseDelivery() {
        Order order = selectOrder();
        if (order == null) {
            return;
        }
        List<DeliveryMethod> methods = service.getDeliveryMethods();
        printer.printDeliveryMethods(methods, order);
        int index = input.readInt("Способ доставки: ", 1, methods.size());
        DeliveryMethod method = methods.get(index - 1);

        service.chooseDeliveryMethod(order.getId(), method);
        printer.printDeliverySummary(order);
    }

    private void assignCourier() {
        Order order = selectOrder();
        if (order == null) {
            return;
        }
        if (order.getDeliveryMethod() == null) {
            System.out.println("Сначала выберите способ доставки для этого заказа.");
            return;
        }
        if (!order.needsCourier()) {
            System.out.println("Для способа \"" + order.getDeliveryMethod().getName() + "\" курьер не нужен.");
            return;
        }
        List<Courier> free = service.getFreeCouriers();
        if (free.isEmpty()) {
            System.out.println("Свободных курьеров сейчас нет.");
            return;
        }
        printer.printCouriers(free);
        int courierId = input.readPositiveInt("Номер курьера: ");
        service.assignCourier(order.getId(), courierId);
        System.out.println("Курьер назначен: " + order.getCourier());
    }

    private void changeStatus() {
        Order order = selectOrder();
        if (order == null) {
            return;
        }
        System.out.println("Текущий статус: " + order.getStatus());
        List<OrderStatus> next = service.getNextStatuses(order.getId());
        if (next.isEmpty()) {
            System.out.println("Заказ уже закрыт, статус больше менять нельзя.");
            return;
        }
        printer.printStatuses(next);
        int index = input.readInt("Новый статус: ", 1, next.size());
        service.changeStatus(order.getId(), next.get(index - 1));
        printer.printOrder(order);
    }

    private void showOrder() {
        Order order = selectOrder();
        if (order != null) {
            printer.printOrder(order);
        }
    }

    /**
     * Спрашивает номер заказа. Возвращает null, если заказов ещё нет.
     */
    private Order selectOrder() {
        if (!service.hasOrders()) {
            System.out.println("Заказов пока нет, сначала создайте заказ.");
            return null;
        }
        printer.printOrderList(service.getAllOrders());
        int id = input.readPositiveInt("Номер заказа: ");
        return service.getOrder(id);
    }
}
