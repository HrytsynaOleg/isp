package controller.testClass;

import dto.DtoUser;
import dto.builder.DtoUserBuilder;
import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;

import javax.servlet.http.HttpServletRequest;

public class TestDtoUser {

    public static DtoUser getDtoUser(HttpServletRequest request) {


        DtoUserBuilder builder = new DtoUserBuilder();

        builder.setUserEmail(request.getParameter("login"));
//        builder.setUserPassword(request.getParameter("password"));
//        builder.setUserConfirmPassword(request.getParameter("confirm"));
        builder.setUserName(request.getParameter("name"));
        builder.setUserLastName(request.getParameter("lastName"));
        builder.setUserPhone(request.getParameter("phone"));
        builder.setUserAdress(request.getParameter("address"));

        return  builder.build();

    }

}

