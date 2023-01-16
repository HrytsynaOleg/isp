package service.impl;

import com.itextpdf.text.DocumentException;
import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import price.IPriceBuilder;
import price.PriceFactory;
import service.IPriceService;

import java.io.FileNotFoundException;
import java.util.List;

public class PriceService implements IPriceService {
    @Override
    public String createPrice(List<Tariff> tariffList,FileFormat format) throws BuildPriceException {
        PriceFactory factory = PriceFactory.getInstance();
        IPriceBuilder priceBuilder = factory.getPriceBuilder(format);
        return priceBuilder.buildPrice(tariffList);
    }
}
