package delivery.model;

/**
 * Статусы заказа и допустимые переходы между ними.
 * <p>
 * NEW -> CONFIRMED -> SHIPPED -> COMPLETED, отменить можно до отправки.
 */
public enum OrderStatus {
    NEW("Новый"),
    CONFIRMED("Подтверждён"),
    SHIPPED("Отправлен"),
    COMPLETED("Выполнен"),
    CANCELLED("Отменён");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    /**
     * Проверяет, можно ли перейти из текущего статуса в указанный.
     */
    public boolean canChangeTo(OrderStatus next) {
        return switch (this) {
            case NEW -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == SHIPPED || next == CANCELLED;
            case SHIPPED -> next == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
