package service;

import dto.DtoService;
import dto.DtoUser;
import entity.Service;
import entity.User;
import enums.SortOrder;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.RelatedRecordsExistException;

import java.util.List;
import java.util.NoSuchElementException;

public interface IServicesService {
    Service getService(int id) throws DbConnectionException, NoSuchElementException;
    Service addService(DtoService dtoService) throws IncorrectFormatException, DbConnectionException;
    void deleteService(int id) throws IncorrectFormatException, DbConnectionException, RelatedRecordsExistException;
    Service updateService (DtoService dtoService) throws IncorrectFormatException, DbConnectionException;
    List<Service> getServicesList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException;
    List<Service> getAllServicesList() throws DbConnectionException;
    List<Service> getFindServicesList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException;
    Integer getServicesCount() throws DbConnectionException;
    Integer getFindServicesCount(int field, String criteria) throws DbConnectionException;

}
