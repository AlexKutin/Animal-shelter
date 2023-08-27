package pro.sky.animalshelter.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.ProbationStatus;

@Component
public class EnumProbationStatusConverter implements Converter<String, ProbationStatus> {
    @Override
    public ProbationStatus convert(@NotNull String source) {
        for (ProbationStatus status : ProbationStatus.values()) {
            if (status.getText().equals(source)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус усыновителя ProbationStatus = " + source);
    }
}
