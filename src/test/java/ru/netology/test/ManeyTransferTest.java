package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManeyTransferTest {
    private DashboardPage dashboardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo).getCode();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
    }
    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        int firstCardBalance = dashboardPage.getCardBalance(firstCard);
        int secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = firstCardBalance / 2;
        TransferPage transferPage = dashboardPage.selectCard(secondCard);
        dashboardPage = transferPage.makeSuccessfulTransfer(String.valueOf(amount), firstCard.getCardNumber());
        assertEquals(firstCardBalance - amount, dashboardPage.getCardBalance(firstCard));
        assertEquals(secondCardBalance + amount, dashboardPage.getCardBalance(secondCard));
    }
    @Test
    void shouldTransferMoneyBetweenCardsIfBalanceLimitExceeded() {
        int firstCardBalance = dashboardPage.getCardBalance(firstCard);
        int secondCardBalance = dashboardPage.getCardBalance(secondCard);
        int amount = firstCardBalance + 1000;
                TransferPage transferPage = dashboardPage.selectCard(secondCard);
        transferPage.makeTransferWithError(String.valueOf(amount), firstCard.getCardNumber());
        dashboardPage = transferPage.cancelTransfer();
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCard),
                "Баланс первой карты не должен измениться при ошибке перевода");
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCard),
                "Баланс второй карты не должен измениться при ошибке перевода");
    }
}