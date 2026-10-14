package delivery.model;

/**
 * Клиент, который оформляет заказ.
 */
public class Client {
    private final String name;
    private final String phone;

    public Client(String name, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        if (phone == null) {
            throw new IllegalArgumentException("Не указан телефон");
        }
        // пробелы, скобки и дефисы просто выкидываем, чтобы можно было вводить как удобно
        String cleanPhone = phone.replaceAll("[\\s()-]", "");
        if (!cleanPhone.matches("\\+?\\d{10,12}")) {
            throw new IllegalArgumentException("Телефон должен содержать 10-12 цифр, например +79991234567");
        }
        this.name = name.trim();
        this.phone = cleanPhone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + ", тел. " + phone;
    }
}
