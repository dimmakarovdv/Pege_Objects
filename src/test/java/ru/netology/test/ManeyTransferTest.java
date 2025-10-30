package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManeyTransferTest {
    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo).getCode();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
        int firstCardBalance = dashboardPage.getCardBalance(firstCard);
        int secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = firstCardBalance / 2;
        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard.getCardNumber());
        assertEquals(firstCardBalance - amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalance + amount, dashboardPage.getCardBalance(secondCard));
    }
    @Test
    void shouldTransferMoneyBetweenCardsIfBalanceLimitExceeded() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo).getCode();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
        int firstCardBalance = dashboardPage.getCardBalance(firstCard);
        int secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = firstCardBalance + 10000;
        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard.getCardNumber());
        $("[data-test-id=error-notification]").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Ошибка"));
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCard),
                "Баланс первой карты не должен измениться при этой ошибке");
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCard),
                "Баланс второй карты не должен измениться при этой ошибке");
    }
}