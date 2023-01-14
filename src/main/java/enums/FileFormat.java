package enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileFormat {
    PDF, TXT, DOCX;

    public static List<String> getFileFormatList () {
       return Arrays.stream(FileFormat.values()).map(Enum::toString).collect(Collectors.toList());
    }
}
