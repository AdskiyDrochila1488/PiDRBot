package entity;

import adapter.message.MessageI;
import adapter.message.PhotoMessage;
import adapter.message.TextMessage;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Екземпляр класу questions матиме два параметри,
// Їх ми пробуєм записати в масив [] об'єктів questions {} в JSON
public class Question {
    public static final String NULL_IMAGE = "null image";
    public static final String CAPTION = "Caption";
    public static final String CORRECT_BUTTON = "CorrectButton";
    public static final String URL = "Url";
    public static final String COUNT_OF_BUTTON = "CountOfButton";
    private final String caption;
    private final String url;
    private final int correctButton;
    private final int countOfButton;

    public Question(JSONObject jsonObject) {
        url = jsonObject.getString(URL);
        correctButton = jsonObject.getInt(CORRECT_BUTTON);
        countOfButton = jsonObject.getInt(COUNT_OF_BUTTON);
        caption = jsonObject.getString(CAPTION);
    }

    public Question(String image, int correctButton, String caption, int button) {
        this.url = image;
        this.correctButton = correctButton;
        this.caption = caption;
        this.countOfButton = button;
    }

    private boolean hasPhoto() {
        return !url.equals(NULL_IMAGE);
    }

    public MessageI createMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboardButtonList();
        MessageI messageI;
        if (hasPhoto()) {
            messageI = new PhotoMessage(chatId, caption, new InputFile(url), inlineKeyboardMarkup);
        } else {
            messageI = new TextMessage(chatId, caption, inlineKeyboardMarkup);
        }
        return messageI;
    }

    private InlineKeyboardMarkup createInlineKeyboardButtonList() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardMarkup in = new InlineKeyboardMarkup();
        for (int i = 1; i < countOfButton; i++) {
            buttons.add(InlineKeyboardButton.builder()
                    .callbackData(i == correctButton ? "true" : "false")
                    .text(i + "✅")
                    .build());
        }
        in.setKeyboard(Collections.singletonList((buttons)));
        return in;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CAPTION, caption);
        jsonObject.put(CORRECT_BUTTON, correctButton);
        jsonObject.put(URL, url);
        jsonObject.put(COUNT_OF_BUTTON, correctButton);
        return jsonObject;
    }
}
