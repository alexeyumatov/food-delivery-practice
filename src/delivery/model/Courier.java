package delivery.model;

/**
 * Курьер. Одновременно может везти только один заказ.
 */
public class Courier {
    private final int id;
    private final String name;
    private final String phone;
    private boolean busy;

    public Courier(int id, String name, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя курьера не может быть пустым");
        }
        this.id = id;
        this.name = name.trim();
        this.phone = phone;
    }

    /**
     * Закрепляет за курьером заказ.
     *
     * @throws IllegalStateException если курьер уже занят другим заказом
     */
    public void takeOrder() {
        if (busy) {
            throw new IllegalStateException("Курьер " + name + " сейчас занят");
        }
        busy = true;
    }

    /**
     * Освобождает курьера после выполнения или отмены заказа.
     */
    public void release() {
        busy = false;
    }

    public boolean isFree() {
        return !busy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
