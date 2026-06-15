package moneytransfer.model;

/**
 * @author Elena Chinarina
 *
 **/

public enum TransferStatus {
    SUCCESS("Успешно"),
    INSUFFICIENT_FUNDS("Недостаточно средств"),
    INVALID_ACCOUNT("Невалидный номер счета"),
    INVALID_AMOUNT("Невалидная сумма"),
    SAME_ACCOUNT("Счета отправителя и получателя совпадают"),
    FILE_ERROR("Ошибка обработки файла"),
    PARSE_ERROR("Ошибка парсинга данных");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
