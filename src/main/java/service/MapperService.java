package service;

import dao.impl.ServiceDao;
import repository.IServicesRepository;
import repository.impl.ServicesRepositoryImpl;
import dto.DtoService;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.TariffStatus;
import exceptions.DbConnectionException;

import java.math.BigDecimal;
import java.sql.SQLException;

public class MapperService {

    static IServicesRepository servicesDao = new ServicesRepositoryImpl(new ServiceDao());

    public static Tariff toTariff(DtoTariff dtoTariff) throws DbConnectionException {
        try {
            Service service = servicesDao.getServiceById(Integer.parseInt(dtoTariff.getService()));
            int id = dtoTariff.getId().equals("")?0:Integer.parseInt(dtoTariff.getId());
            return new Tariff(
                    id,
                    service, dtoTariff.getName(),
                    dtoTariff.getDescription(),
                    new BigDecimal(dtoTariff.getPrice()),
                    BillingPeriod.valueOf(dtoTariff.getPeriod()),
                    TariffStatus.valueOf(dtoTariff.getStatus()));
        } catch (SQLException e) {
            throw new DbConnectionException("alert.databaseError");
        }
    }

    public static  DtoTariff toDtoTariff(Tariff tariff) {

        return new DtoTariff(
                String.valueOf(tariff.getId()),
                tariff.getName(),
                tariff.getDescription(),
                String.valueOf(tariff.getService().getId()),
                tariff.getStatus().toString(),
                tariff.getPrice().toString(),
                tariff.getPeriod().toString());
    }

    public static  Service toService(DtoService dtoService) {
        int id = dtoService.getId().equals("")?0:Integer.parseInt(dtoService.getId());
        return new Service(
                id,
                dtoService.getName(),
                dtoService.getDescription());
    }

}
