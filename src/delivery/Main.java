package delivery;

import delivery.console.ConsoleInput;
import delivery.console.ConsoleMenu;
import delivery.console.OrderPrinter;
import delivery.service.OrderService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        OrderService service = new OrderService();
        service.addCourier("Иван Петров", "+79001112233");
        service.addCourier("Ольга Смирнова", "+79004445566");
        service.addCourier("Артём Козлов", "+79007778899");

        ConsoleInput input = new ConsoleInput(new Scanner(System.in));
        new ConsoleMenu(service, input, new OrderPrinter()).run();
    }
}
