package ru.bit.mybit.server;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.URL;
import org.telegram.telegrambots.ApiContextInitializer;
//   import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.bit.mybit.server.telegram.Emoji;

public class MyBitBot extends TelegramLongPollingBot {

    boolean enabled = true;
    private Double threshhold = 1.0;

    String username = "MyBit0kBot";
    String token = "390134287:AAF6bJ5R8UlWKKJZi0ERw0nbM56oWg00zys";
    //String username = "MyBit0kLocalBot";
    //String token = "356041603:AAFYlfpGMdpcDZIA7xuHm625VaCseiPUY-s";

    @Override
    public String getBotUsername() { return username; }
    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().equals("/help"))
                sendMsg(message, "Hello!");
        }
    }

    public MyBitBot() {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            sendMsg("Bot started with threshold " + threshhold.toString());
        } catch (TelegramApiRequestException e) {
            System.out.println(e.getMessage());
        }
    }

    public Double getThreshold() {
        return threshhold;
    }

    public Double setThreshold(Double threshhold) {
        sendMsg("Threshold changed from " + this.threshhold.toString() + " to " + threshhold);
        this.threshhold = threshhold;
        return this.threshhold;
    }

    public void enableBot() {
        enabled = true;
        sendMsg("Bot Enabled");
        poloAlert = false;
        krakenAlert = false;
        bittrexxAlert = false;
        bitstampAlert = false;
    }

    public void disableBot() {
        sendMsg("Bot Disabled");
        enabled = false;
    }

    public void toggleBot() {
        if (enabled)
            disableBot();
        else enableBot();
        System.out.println("Bot Toggled: " + enabled);
    }

    public String isEnabled() {
        return enabled ? "Bot is ON" : "Bot is OFF";
    }


    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String text) {
        if (enabled) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId("-202032213");
            sendMessage.setText(text);
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    boolean poloAlert = false;
    boolean bittrexxAlert = false;
    boolean krakenAlert = false;
    boolean bitstampAlert = false;

    String good = Emoji.GRINNING_FACE_WITH_SMILING_EYES.toString();
    String bad = Emoji.TIRED_FACE.toString();

    private String createMessage(String exchange, boolean alarm, Double spread, BigDecimal longBid, BigDecimal shortAsk) {

        DecimalFormat df = new DecimalFormat("###,###.00");

        return exchange + " " + df.format(spread) + "% " + (alarm ? (good + ">") : (bad + "<")) +
                "Bid: " + longBid.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "\n" +
                "Ask (Bitfinex): " + shortAsk.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " ";
    }

    public void raisePoloAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (!poloAlert) {
            poloAlert = true;
            sendMsg(createMessage("POLO", poloAlert, spread, longBid, shortAsk));

        }
    }

    public void raiseBittrexAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (!bittrexxAlert) {
            bittrexxAlert = true;
            sendMsg(createMessage("BITTREX", bittrexxAlert, spread, longBid, shortAsk));

        }
    }

    public void raiseKrakenAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (!krakenAlert) {
            krakenAlert = true;
            sendMsg(createMessage("KRAKEN", krakenAlert, spread, longBid, shortAsk));
        }
    }

    public void raiseBitstampAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (!bitstampAlert) {
            bitstampAlert = true;
            sendMsg(createMessage("BITSTAMP", bitstampAlert, spread, longBid, shortAsk));
        }
    }

    public void shutdownPoloAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (poloAlert) {
            poloAlert = false;
            sendMsg(createMessage("POLO", poloAlert, spread, longBid, shortAsk));
        }
    }

    public void shutdownBittrexAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (bittrexxAlert) {
            bittrexxAlert = false;
            sendMsg(createMessage("BITTREX", bittrexxAlert, spread, longBid, shortAsk));
        }
    }

    public void shutdownKrakenAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (krakenAlert) {
            krakenAlert = false;
            sendMsg(createMessage("KRAKEN", krakenAlert, spread, longBid, shortAsk));
        }
    }

    public void shutdownBitstampAlert(Double spread, BigDecimal longBid, BigDecimal shortAsk) {
        if (bitstampAlert) {
            bitstampAlert = false;
            sendMsg(createMessage("BITSTAMP", bitstampAlert, spread, longBid, shortAsk));
        }
    }

}
