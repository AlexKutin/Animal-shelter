package pro.sky.animalshelter.keyBoard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupHelper {

    public static InlineKeyboardMarkup createInlineKeyboard(String text, String[] buttonLabels) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (String label : buttonLabels) {
            InlineKeyboardButton button = new InlineKeyboardButton(label).callbackData(label);
            rowInline.add(button);
        }

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }
    public static InlineKeyboardMarkup createMainMenuInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Кнопка "Информация о приюте"
        rowInline.add(new InlineKeyboardButton("1").callbackData("Info"));

        // Кнопка "Как забрать животное"
        rowInline.add(new InlineKeyboardButton("2").callbackData("AdoptionRules"));

        // Кнопка "Отправить отчет о животном"
        rowInline.add(new InlineKeyboardButton("3").callbackData("SendReport"));

        // Кнопка "Позвать волонтера"
        rowInline.add(new InlineKeyboardButton("4").callbackData("CallVolunteer"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }
    public static InlineKeyboardMarkup createShelterInfoInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Add buttons for shelter information options
        rowInline.add(new InlineKeyboardButton("О приюте").callbackData("ShelterInfoAbout"));
        rowInline.add(new InlineKeyboardButton("Расписание и адрес").callbackData("ShelterInfoAddress"));
        rowInline.add(new InlineKeyboardButton("Контакты охраны").callbackData("ShelterInfoSecurityContacts"));
        rowInline.add(new InlineKeyboardButton("Техника безопасности").callbackData("ShelterInfoSafetyTips"));
        rowInline.add(new InlineKeyboardButton("Оставить контакты").callbackData("ShelterInfoLeaveContacts"));
        rowInline.add(new InlineKeyboardButton("Позвать волонтера").callbackData("ShelterInfoCallVolunteer"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    // Method to create inline keyboard for "Adoption rules" options
    public static InlineKeyboardMarkup createAdoptionRulesInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Add buttons for adoption rules options
        rowInline.add(new InlineKeyboardButton("Приветствие").callbackData("AdoptionRulesGreeting"));
        rowInline.add(new InlineKeyboardButton("Правила знакомства").callbackData("AdoptionRulesIntroduction"));
        rowInline.add(new InlineKeyboardButton("Список документов").callbackData("AdoptionRulesDocuments"));
        rowInline.add(new InlineKeyboardButton("Транспортировка животного").callbackData("AdoptionRulesTransport"));
        rowInline.add(new InlineKeyboardButton("Обустройство дома (щенок/котенок)").callbackData("AdoptionRulesHouseSetupPuppyKitten"));
        rowInline.add(new InlineKeyboardButton("Обустройство дома (взрослое животное)").callbackData("AdoptionRulesHouseSetupAdult"));
        rowInline.add(new InlineKeyboardButton("Обустройство дома (с ограничениями)").callbackData("AdoptionRulesHouseSetupSpecialNeeds"));
        // Skip the dog-specific options for a cat shelter

        rowInline.add(new InlineKeyboardButton("Причины отказа").callbackData("AdoptionRulesRejectionReasons"));
        rowInline.add(new InlineKeyboardButton("Оставить контакты").callbackData("AdoptionRulesLeaveContacts"));
        rowInline.add(new InlineKeyboardButton("Позвать волонтера").callbackData("AdoptionRulesCallVolunteer"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }
}
