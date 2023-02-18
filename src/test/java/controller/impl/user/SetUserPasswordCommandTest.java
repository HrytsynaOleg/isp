package controller.impl.user;

import testClass.TestSession;
import testClass.TestUser;
import entity.User;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SetUserPasswordCommandTest {

    IUserService userService = mock(IUserService.class);
    SetUserPasswordCommand setUserPasswordCommand = new SetUserPasswordCommand(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session= new TestSession();
    User testUser;

    @BeforeEach
    void setUp() {
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

    @Test
    void processIfPasswordNotMatch() throws DbConnectionException, IncorrectFormatException {
        doThrow(new IncorrectFormatException("alert.passwordNotMatch")).when(userService).setUserPassword(testUser.getId(),"password", "password");

        String path = setUserPasswordCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
        assertEquals("alert.passwordNotMatch", session.getAttribute("alert"));

    }
}