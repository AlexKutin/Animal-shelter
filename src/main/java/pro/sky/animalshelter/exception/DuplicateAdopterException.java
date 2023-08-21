package pro.sky.animalshelter.exception;

import pro.sky.animalshelter.dto.ErrorDTO;

public class DuplicateAdopterException extends RuntimeException {
    private static final int ERROR_CODE = 10001;    // Adopter duplication error (userId and animalId already present)

    public DuplicateAdopterException(String message) {
        super(message);
    }

    public ErrorDTO toErrorDTO() {
        return new ErrorDTO(this.getMessage(), ERROR_CODE);
    }
}
