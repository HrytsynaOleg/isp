package service;

import dependecies.DependencyManager;
import dto.DtoUser;
import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;
import repository.IServicesRepository;
import dto.DtoService;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import repository.IUserRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class MapperService {

    static IServicesRepository serviceRepo = DependencyManager.serviceRepo;
    static IUserRepository userRepo = DependencyManager.userRepo;

    public static Tariff toTariff(DtoTariff dtoTariff) throws DbConnectionException {
        try {
            Service service = serviceRepo.getServiceById(Integer.parseInt(dtoTariff.getService()));
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

    public static  List<DtoService> toDtoServiceList(List<Service> serviceList) {

        return serviceList.stream().map(e->new DtoService(String.valueOf(e.getId()), e.getName(), e.getDescription())).toList();

    }

    public static User toUser(DtoUser dtoUser) throws SQLException {
        User user;

        try{
            if (dtoUser.getId().equals("")) throw new NoSuchElementException();
            user = userRepo.getUserById(Integer.parseInt(dtoUser.getId()));
            user.setEmail(dtoUser.getEmail());
            user.setName(dtoUser.getName());
            user.setLastName(dtoUser.getLastName());
            user.setPhone(dtoUser.getPhone());
            user.setAdress(dtoUser.getAddress());

        } catch (NoSuchElementException e) {

            UserBuilder builder = new UserBuilder();
            builder.setUserEmail(dtoUser.getEmail());
            builder.setUserName(dtoUser.getName());
            builder.setUserLastName(dtoUser.getLastName());
            builder.setUserPhone(dtoUser.getPhone());
            builder.setUserAdress(dtoUser.getAddress());
            builder.setUserRole(UserRole.valueOf(dtoUser.getRole()));
            user=builder.build();
            user.setRegistration(new Date());
            user.setBalance(BigDecimal.valueOf(0));
        }

        return user;
    }

}
