package delivery.shipping;

/**
 * Экспресс-доставка на следующий день.
 * Стоит фиксированную сумму плюс процент от заказа.
 */
public class ExpressDelivery implements DeliveryMethod {
    private static final double BASE_PRICE = 500;
    private static final double PERCENT = 0.05;

    @Override
    public String getName() {
        return "Экспресс-доставка";
    }

    @Override
    public double calculateCost(double orderSum) {
        return BASE_PRICE + orderSum * PERCENT;
    }

    @Override
    public int getDeliveryDays() {
        return 1;
    }
}
