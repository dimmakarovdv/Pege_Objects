package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManeyTransferTest {
    @Test
    void shouldTransferMoneyBetweenOwncards() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo).getCode();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 1000;
        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard.getCardNumber());
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(firstCard));
        assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(secondCard));
    }
    @Test
    void shouldTransferMoneyBetweenCardsIfBalanceLimitExceeded() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo).getCode();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 20000;
        var transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard.getCardNumber());
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;

        assertEquals(expectedFirstCardBalance, dashboardPage.getCardBalance(firstCard));
        assertEquals(expectedSecondCardBalance, dashboardPage.getCardBalance(secondCard));
    }
}