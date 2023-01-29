package dao;

import dto.DtoService;
import dto.DtoUser;
import entity.Service;
import entity.User;
import exceptions.DbConnectionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface IServiceDao {
    int addService(Service service) throws SQLException;
    Service getServiceByName(String name) throws DbConnectionException, SQLException;
    Service getServiceById(int id) throws NoSuchElementException, SQLException;
    boolean isServiceNameExist(String name) throws SQLException;
    void updateService(DtoService dtoService) throws SQLException;
    void deleteService(int id) throws SQLException;
    List<Service> getServicesList( Map<String,String> parameters) throws SQLException;
    Integer getServicesCount( Map<String,String> parameters) throws SQLException;

}
