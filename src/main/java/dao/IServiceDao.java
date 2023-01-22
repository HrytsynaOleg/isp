package dao;

import dto.DtoService;
import dto.DtoUser;
import entity.Service;
import entity.User;
import exceptions.DbConnectionException;

import java.util.List;
import java.util.Map;

public interface IServiceDao {
    int addService(Service service) throws DbConnectionException;
    Service getServiceByName(String name) throws DbConnectionException;
    Service getServiceById(int id) throws DbConnectionException;
    void updateService(DtoService dtoService) throws DbConnectionException;
    void deleteService(int id) throws DbConnectionException;
    List<Service> getServicesList( Map<String,String> parameters) throws DbConnectionException;
    List<Service> getServicesList() throws DbConnectionException;
    Integer getServicesCount( Map<String,String> parameters) throws DbConnectionException;

}
