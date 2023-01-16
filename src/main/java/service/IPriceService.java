package service;

import entity.Tariff;
import enums.FileFormat;
import exceptions.BuildPriceException;
import java.util.List;

public interface IPriceService {
    String createPrice (List<Tariff> tariffList, FileFormat format) throws BuildPriceException;
}
