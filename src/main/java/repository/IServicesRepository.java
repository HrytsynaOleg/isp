package repository;

import dto.DtoService;
import entity.Service;
import exceptions.DbConnectionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface IServicesRepository {
    int addService(Service service) throws SQLException;
    Service getServiceById(int id) throws NoSuchElementException, SQLException;
    boolean isServiceNameExist(String serviceName) throws SQLException;
    void updateService(Service service) throws SQLException;
    void deleteService(int id) throws SQLException;
    List<Service> getServicesList( Map<String,String> parameters) throws SQLException;
    Integer getServicesCount( Map<String,String> parameters) throws SQLException;

}
