package delivery.shipping;

/**
 * Обычная доставка курьером. При заказе от 3000 руб. бесплатно.
 */
public class StandardDelivery implements DeliveryMethod {
    private static final double PRICE = 250;
    private static final double FREE_FROM = 3000;

    @Override
    public String getName() {
        return "Стандартная доставка";
    }

    @Override
    public double calculateCost(double orderSum) {
        if (orderSum >= FREE_FROM) {
            return 0;
        }
        return PRICE;
    }

    @Override
    public int getDeliveryDays() {
        return 3;
    }
}
