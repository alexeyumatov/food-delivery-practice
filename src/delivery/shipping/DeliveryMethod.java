package delivery.shipping;

/**
 * Способ доставки. Каждая реализация сама считает стоимость и срок.
 */
public interface DeliveryMethod {

    String getName();

    /**
     * Считает стоимость доставки.
     *
     * @param orderSum сумма товаров в заказе
     * @return стоимость доставки в рублях
     */
    double calculateCost(double orderSum);

    /**
     * @return примерный срок доставки в днях
     */
    int getDeliveryDays();

    /**
     * Нужен ли курьер для этого способа. По умолчанию нужен.
     */
    default boolean needsCourier() {
        return true;
    }
}
