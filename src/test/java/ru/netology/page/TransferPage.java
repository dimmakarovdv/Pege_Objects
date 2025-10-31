package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountInput = $("[data-test-id=amount] input");
    private SelenideElement fromInput = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public TransferPage() {
        $("[data-test-id=amount]").shouldBe(Condition.visible);
    }
    public DashboardPage makeSuccessfulTransfer(String amount, String fromCard) {
        makeTransfer(amount, fromCard);
        return new DashboardPage();
    }
    public TransferPage makeTransferWithError(String amount, String fromCard) {
        makeTransfer(amount, fromCard);
        checkErrorNotificationVisible();
        return this;
    }
    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }
    private void makeTransfer(String amount, String fromCard) {
        amountInput.setValue(amount);
        fromInput.setValue(fromCard);
        transferButton.click();
    }
    private void checkErrorNotificationVisible() {
        errorNotification.shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка"));
    }
}