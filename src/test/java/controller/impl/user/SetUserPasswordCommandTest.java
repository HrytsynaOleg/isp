package controller.impl.user;

import controller.testClass.TestSession;
import controller.testClass.TestUser;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static controller.manager.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SetUserPasswordCommandTest {
    SetUserPasswordCommand setUserPasswordCommand;
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        setUserPasswordCommand = spy(new SetUserPasswordCommand());
        Whitebox.setInternalState(SetUserPasswordCommand.class, "service", userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getAdmin();
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirm")).thenReturn("password");
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {


        doNothing().when(userService).setUserPassword(testUser.getId(),"password", "password");

        String path = setUserPasswordCommand.process(request, response);

        assertEquals("controller?command=profilePage", path);
        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
        assertEquals("info.userPasswordChanged", session.getAttribute("info"));

    }

    @Test
    void processIfInputValuesNotValid() throws DbConnectionException, IncorrectFormatException {
        doThrow(new IncorrectFormatException("alert.incorrectPassword")).when(userService).setUserPassword(testUser.getId(),"password", "password");

        String path = setUserPasswordCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
        assertEquals("alert.incorrectPassword", session.getAttribute("alert"));

    }
}