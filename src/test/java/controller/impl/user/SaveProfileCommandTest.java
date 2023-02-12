package controller.impl.user;

import controller.testClass.TestDtoUser;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoUser;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SaveProfileCommandTest {
    IUserService userService = mock(IUserService.class);
    SaveProfileCommand saveProfileCommand = new SaveProfileCommand(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = new TestSession();
    User testUser = TestUser.getAdmin();
    User newUser = TestUser.getAdmin();
    DtoUser dtoUser;

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
        newUser.setEmail("user@test.com");
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("login")).thenReturn("user@test.com");
        dtoUser = TestDtoUser.getDtoUser(request);
        dtoUser.setId(String.valueOf(testUser.getId()));
    }

    @Test
    void process() throws DbConnectionException, IncorrectFormatException {

        when(userService.isUserExist("user@test.com")).thenReturn(false);
        when(userService.updateUser(dtoUser)).thenReturn(newUser);

        String path = saveProfileCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(newUser, session.getAttribute("loggedUser"));
        assertEquals(newUser.getRole(), session.getAttribute("role"));
        assertEquals(getPathName("content.profile"), session.getAttribute("contentPage"));
        assertEquals("info.profileUpdate", session.getAttribute("info"));
        assertNull(session.getAttribute("user"));
    }

    @Test
    void processIfUserExist() throws DbConnectionException {

        when(userService.isUserExist("user@test.com")).thenReturn(true);

        String path = saveProfileCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.editProfile"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.userAlreadyRegistered", session.getAttribute("alert"));
    }

    @Test
    void processIfInputValuesNotValid() throws DbConnectionException, IncorrectFormatException {

        when(userService.isUserExist("user@test.com")).thenReturn(false);
        doThrow(new IncorrectFormatException("alert.incorrectEmail")).when(userService).updateUser(dtoUser);

        String path = saveProfileCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.editProfile"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.incorrectEmail", session.getAttribute("alert"));

    }
}