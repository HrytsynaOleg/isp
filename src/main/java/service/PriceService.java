package service;

import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import price.IPriceBuilder;
import price.PriceFactory;
import java.util.List;

public class PriceService {

    private static PriceService instance;
    private PriceService() {
    }

    public String createPrice(List<Tariff> tariffList, FileFormat format) throws BuildPriceException {
        PriceFactory factory = PriceFactory.getInstance();
        IPriceBuilder priceBuilder = factory.getPriceBuilder(format);
        return priceBuilder.buildPrice(tariffList);
    }

    public static synchronized PriceService getInstance() {
        if (instance==null) {
            instance=new PriceService();
        }
        return instance;
    }
}
