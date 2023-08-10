package pro.sky.animalshelter.keyBoard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupHelper {

    public static InlineKeyboardMarkup createInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("Приют кошек\uD83D\uDC31").callbackData("Приют кошек\uD83D\uDC31"));
        rowInline.add(new InlineKeyboardButton("Приют собак\uD83D\uDC36").callbackData("Приют собак\uD83D\uDC36"));
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
        // Кнопка "⬅ Назад"
        rowInline.add(new InlineKeyboardButton("⬅").callbackData("BackShelterMenu"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createShelterInfoInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("1").callbackData("ShelterInfoAbout"));
        rowInline.add(new InlineKeyboardButton("2").callbackData("ShelterInfoAddress"));
        rowInline.add(new InlineKeyboardButton("3").callbackData("ShelterContacts"));
        rowInline.add(new InlineKeyboardButton("4").callbackData("ShelterInfoSecurityContacts"));
        rowInline.add(new InlineKeyboardButton("5").callbackData("ShelterInfoSafetyTips"));
        rowInline.add(new InlineKeyboardButton("6").callbackData("ShelterInfoLeaveContacts"));
        rowInline.add(new InlineKeyboardButton("7").callbackData("CallVolunteer"));
        rowInline.add(new InlineKeyboardButton("⬅").callbackData("BackMainMenu"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    // Method to create inline keyboard for "Adoption rules" options
    public static InlineKeyboardMarkup createAdoptionRulesInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("1").callbackData("AdoptionRulesIntroduction"));
        rowInline.add(new InlineKeyboardButton("2").callbackData("AdoptionRulesDocuments"));
        rowInline.add(new InlineKeyboardButton("3").callbackData("AdoptionRulesTransport"));
        rowInline.add(new InlineKeyboardButton("4").callbackData("AdoptionRulesHouseSetup"));
        rowInline.add(new InlineKeyboardButton("5").callbackData("AdoptionRulesRejectionReasons"));
        rowInline.add(new InlineKeyboardButton("6").callbackData("CallVolunteer"));
        rowInline.add(new InlineKeyboardButton("⬅").callbackData("BackMainMenu"));
        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createAdoptionRulesForDogInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("1").callbackData("AdoptionRulesIntroduction"));
        rowInline.add(new InlineKeyboardButton("2").callbackData("AdoptionRulesDocuments"));
        rowInline.add(new InlineKeyboardButton("3").callbackData("AdoptionRulesTransport"));
        rowInline.add(new InlineKeyboardButton("4").callbackData("AdoptionRulesHouseSetup"));
        rowInline.add(new InlineKeyboardButton("5").callbackData("AdoptionRulesRejectionReasons"));
        rowInline.add(new InlineKeyboardButton("6").callbackData("Cynologist"));
        rowInline.add(new InlineKeyboardButton("7").callbackData("CallVolunteer"));
        rowInline.add(new InlineKeyboardButton("⬅").callbackData("BackMainMenu"));
        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createBackToShelterInfoInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackShelterInfo"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createCancelContactInputInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("Отмена").callbackData("CancelContactInput"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createBackToAdoptionRulesInlineKeyboard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackAdoptionRules"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createPetHouseSelectionKeyBoard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("1").callbackData("AdoptionRulesHouseSetupPuppyKitten"));
        rowInline.add(new InlineKeyboardButton("2").callbackData("AdoptionRulesHouseSetupAdult"));
        rowInline.add(new InlineKeyboardButton("3").callbackData("AdoptionRulesHouseSetupSpecialNeeds"));
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackAdoptionRules"));
        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createBackToPetHouseSelectionKeyBoard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackToPetHouseSelection"));
        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }

    public static InlineKeyboardMarkup createCynologistKeyBoard() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("1").callbackData("AdviceFromCynologist"));
        rowInline.add(new InlineKeyboardButton("2").callbackData("ListCynologist"));
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackAdoptionRules"));
        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }
    public static InlineKeyboardMarkup createBackToCynologist() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton("⬅ Назад").callbackData("BackToCynologist"));

        InlineKeyboardButton[] buttonsArray = rowInline.toArray(new InlineKeyboardButton[0]);
        return new InlineKeyboardMarkup(buttonsArray);
    }
}