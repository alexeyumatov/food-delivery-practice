package delivery.shipping;

/**
 * Самовывоз из пункта выдачи. Бесплатно, курьер не нужен.
 */
public class Pickup implements DeliveryMethod {

    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public double calculateCost(double orderSum) {
        return 0;
    }

    @Override
    public int getDeliveryDays() {
        return 2;
    }

    @Override
    public boolean needsCourier() {
        return false;
    }
}
