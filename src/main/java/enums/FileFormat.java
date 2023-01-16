package enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileFormat {
    PDF("application/pdf", "PdfPrice.pdf"),
//    XLS("application/vnd.ms-excel", "XlsPrice.xls"),
//    TXT("text/plain", "TxtPrice.txt"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "DocxPrice.docx");

    private final String contentType;
    private final String fileName;

    FileFormat(String contentType, String fileName) {
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public static List<String> getFileFormatList () {
       return Arrays.stream(FileFormat.values()).map(Enum::toString).collect(Collectors.toList());
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }
}
