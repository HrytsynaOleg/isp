package price.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import price.IPriceBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PdfPriceBuilder implements IPriceBuilder {
    @Override
    public String buildPrice(List<Tariff> tariffList) throws BuildPriceException {

        Document document = new Document();
        String sRootPath = new File("").getAbsolutePath();
        String filePath = sRootPath + "/temp/" + FileFormat.PDF.getFileName();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Font fontTable = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
            Chunk chunk = new Chunk("ISP services and prices", font);

            PdfPTable table = new PdfPTable(5);
            Stream.of("Service", "Tariff", "Description", "Price", "Period")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, fontTable));
                        table.addCell(header);
                    });
            for (Tariff tariff : tariffList) {
                table.addCell(new Paragraph(tariff.getService().getName(), fontTable));
                table.addCell(new Paragraph(tariff.getName(), fontTable));
                table.addCell(new Paragraph(tariff.getDescription(), fontTable));
                table.addCell(new Paragraph(String.valueOf(tariff.getPrice()), fontTable));
                table.addCell(new Paragraph(tariff.getPeriod().name(), fontTable));
            }

            document.add(new Phrase(chunk));
            document.add(table);
            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            throw new BuildPriceException("alert.priceBuildError");
        }

        return filePath;
    }

    @Override
    public FileFormat getPriceFormat() {
        return FileFormat.PDF;
    }
}
