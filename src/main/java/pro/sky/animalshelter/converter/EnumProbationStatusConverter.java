package pro.sky.animalshelter.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.PROBATION_STATUS;

@Component
public class EnumProbationStatusConverter implements Converter<String, PROBATION_STATUS> {
    @Override
    public PROBATION_STATUS convert(@NotNull String source) {
        for (PROBATION_STATUS status : PROBATION_STATUS.values()) {
            if (status.getText().equals(source)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус усыновителя PROBATION_STATUS = " + source);
    }
}
