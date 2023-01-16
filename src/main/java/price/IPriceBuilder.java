package price;

import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import java.util.List;

public interface IPriceBuilder {
    String buildPrice(List<Tariff> tariffList) throws BuildPriceException;
    FileFormat getPriceFormat();
}
