package delivery.model;

/**
 * Позиция в заказе: товар, количество и цена за штуку.
 */
public class OrderItem {
    private final String productName;
    private final int quantity;
    private final double price;

    public OrderItem(String productName, int quantity, double price) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Название товара не может быть пустым");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть больше нуля");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Цена должна быть больше нуля");
        }
        this.productName = productName.trim();
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotal() {
        return price * quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
