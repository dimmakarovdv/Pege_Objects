package ru.netology.page;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.*;

public class TransferPage {
    public TransferPage() {
        $("[data-test-id=amount]").shouldBe(Condition.visible);
    }

    public void makeTransfer(String amount, String fromCard) {
        $("[data-test-id=amount] input").setValue(amount);
        $("[data-test-id=from] input").setValue(fromCard);
        $("[data-test-id=action-transfer]").click();
    }
}