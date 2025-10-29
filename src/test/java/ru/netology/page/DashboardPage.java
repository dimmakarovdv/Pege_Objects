package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

    public class DashboardPage {
        private final SelenideElement header = $("[data-test-id=dashboard]");
        private ElementsCollection cards = $$(".list__item");
        private final String balanceStart = "баланс: ";
        private final String balanceFinish = " р.";

        public DashboardPage() {
            header.should(Condition.visible);
        }

        private int extractBalance(String text) {
            var start = text.indexOf(balanceStart);
            var finish = text.indexOf(balanceFinish);
            var value = text.substring(start + balanceStart.length(), finish).trim();
            return Integer.parseInt(value);
        }

        private SelenideElement getCard(DataHelper.CardInfo cardInfo) {
            return cards.find(Condition.attribute("data-test-id", cardInfo.getTestId()));
        }

        public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
            var lastFourDigits = cardInfo.getCardNumber().substring(cardInfo.getCardNumber().length() - 4);
            cards.find(Condition.text(lastFourDigits)).$("button").click();
            return new TransferPage();
        }

        public int getCardBalance(DataHelper.CardInfo cardInfo) {
            var lastFourDigits = cardInfo.getCardNumber().substring(cardInfo.getCardNumber().length() - 4);
            var cardElement = cards.find(Condition.text(lastFourDigits))
                    .shouldBe(Condition.visible, Duration.ofSeconds(15));
            var text = cardElement.getText();
            return extractBalance(text);
        }
    }
