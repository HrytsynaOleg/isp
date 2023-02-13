package price.impl;

import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import price.IPriceBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class DocxPriceBuilder implements IPriceBuilder {
    @Override
    public String buildPrice(List<Tariff> tariffList) throws BuildPriceException {

        String sRootPath = new File("").getAbsolutePath();
        String filePath = sRootPath + "/temp/" + FileFormat.DOCX.getFileName();

        try {
            WordprocessingMLPackage wordPackage;
            try {
                wordPackage = WordprocessingMLPackage.createPackage();
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addParagraphOfText("ISP services and prices");
            int writableWidthTwips = wordPackage.getDocumentModel()
                    .getSections().get(0).getPageDimensions().getWritableWidthTwips();
            int columnNumber = 5;
            int rowNumber = tariffList.size() + 1;
            Tbl tbl = TblFactory.createTable(rowNumber, columnNumber, writableWidthTwips / columnNumber);
            List<Object> rows = tbl.getContent();
            Tr tableHeader = (Tr) rows.get(0);
            List<String> headerStrings = Arrays.asList("Service", "Tariff", "Description", "Price", "Period");
            fillRow(tableHeader, headerStrings);

            int counter = 1;
            for (Tariff tariff : tariffList) {
                Tr row = (Tr) rows.get(counter++);
                List<String> rowStrings = Arrays.asList(tariff.getService().getName(), tariff.getName(),
                        tariff.getDescription(), String.valueOf(tariff.getPrice()), tariff.getPeriod().name());
                fillRow(row, rowStrings);
            }
            mainDocumentPart.addObject(tbl);

            File exportFile = new File(filePath);

            wordPackage.save(exportFile);
        } catch (Docx4JException e) {
            throw new BuildPriceException("alert.priceBuildError");
        }

        return filePath;
    }

    @Override
    public FileFormat getPriceFormat() {
        return FileFormat.DOCX;
    }

    private void fillRow(Tr row, List<String> rowStrings) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        List<Object> cellsRow = row.getContent();
        int counter = 0;
        for (Object cell : cellsRow) {
            Tc td = (Tc) cell;
            P p = factory.createP();
            R r = factory.createR();
            Text t = factory.createText();
            t.setValue(rowStrings.get(counter++));
            r.getContent().add(t);
            p.getContent().add(r);
            td.getContent().add(p);
        }
    }
}
