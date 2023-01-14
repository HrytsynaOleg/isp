package enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TariffStatus {
    ACTIVE, SUSPENDED, ARCHIVE;

    public static List<String> getStatusList () {
       return Arrays.stream(TariffStatus.values()).map(Enum::toString).collect(Collectors.toList());
    }
}
