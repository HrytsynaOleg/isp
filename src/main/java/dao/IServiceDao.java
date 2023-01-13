package dao;

import dto.DtoService;
import dto.DtoUser;
import entity.Service;
import entity.User;
import exceptions.DbConnectionException;

import java.util.List;

public interface IServiceDao {
    int addService(Service service) throws DbConnectionException;
    Service getServiceByName(String name) throws DbConnectionException;
    Service getServiceById(int id) throws DbConnectionException;
    void updateService(DtoService dtoService) throws DbConnectionException;
    void deleteService(int id) throws DbConnectionException;
    List<Service> getServicesList(Integer limit, Integer total, Integer sort, String order) throws DbConnectionException;
    List<Service> getServicesList() throws DbConnectionException;
    List<Service> getFindServicesList(Integer limit, Integer total, Integer sort, String order, int field, String criteria) throws DbConnectionException;
    Integer getServicesCount() throws DbConnectionException;
    Integer getFindServicesCount(int field, String criteria) throws DbConnectionException;
}
