package pro.sky.animalshelter.dto;

public class ErrorDTO {
    private final String message;
    private final int errorCode;

    public ErrorDTO(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
