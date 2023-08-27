package pro.sky.animalshelter.converter;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pro.sky.animalshelter.model.ReportStatus;

@Component
public class EnumReportStatusConverter implements Converter<String, ReportStatus> {
    @Override
    public ReportStatus convert(@NotNull String source) {
        for (ReportStatus status : ReportStatus.values()) {
            if (status.getText().equals(source)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус усыновителя ReportStatus = " + source);
    }
}
