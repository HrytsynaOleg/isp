package controller.impl.user;

import controller.testClass.TestDtoUser;
import controller.testClass.TestSession;
import controller.testClass.TestUser;
import dto.DtoUser;
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

import static settings.properties.PathNameManager.getPathName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SaveProfileCommandTest {
    SaveProfileCommand saveProfileCommand;
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    User testUser;
    User newUser;
    DtoUser dtoUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        saveProfileCommand = spy(new SaveProfileCommand());
        Whitebox.setInternalState(SaveProfileCommand.class, "service", userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = new TestSession();
        when(request.getSession()).thenReturn(session);
        testUser = TestUser.getAdmin();
        newUser = TestUser.getAdmin();
        newUser.setEmail("user@test.com");
        session.setAttribute("loggedUser", testUser);
        when(request.getParameter("login")).thenReturn("user@test.com");
        dtoUser = TestDtoUser.getDtoUser(request);
        dtoUser.setId(String.valueOf(testUser.getId()));
    }

    @Test
    void process() throws UserAlreadyExistException, DbConnectionException, IncorrectFormatException {

        doNothing().when(saveProfileCommand).validateIsUserRegistered("user@test.com");
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
    void processIfUserExist() throws DbConnectionException, IncorrectFormatException, UserAlreadyExistException {

        doThrow(new UserAlreadyExistException("alert.userAlreadyRegistered")).when(saveProfileCommand).validateIsUserRegistered("user@test.com");
        when(userService.updateUser(dtoUser)).thenReturn(newUser);

        String path = saveProfileCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.editProfile"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.userAlreadyRegistered", session.getAttribute("alert"));
    }

    @Test
    void processIfInputValuesNotValid() throws DbConnectionException, IncorrectFormatException, UserAlreadyExistException {

        doNothing().when(saveProfileCommand).validateIsUserRegistered("user@test.com");
        doThrow(new IncorrectFormatException("alert.incorrectEmail")).when(userService).updateUser(dtoUser);

        String path = saveProfileCommand.process(request, response);

        assertEquals("admin.jsp", path);
        assertEquals(getPathName("content.editProfile"), session.getAttribute("contentPage"));
        assertEquals(dtoUser, session.getAttribute("user"));
        assertEquals("alert.incorrectEmail", session.getAttribute("alert"));

    }
}