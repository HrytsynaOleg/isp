package controller;

import controller.ICommand;
import controller.impl.service.AddServicePageCommand;
import controller.impl.service.CreateServiceCommand;
import controller.impl.table.ServicesListPageCommand;
import controller.impl.table.TariffsListPageCommand;
import controller.impl.table.UserListPageCommand;
import controller.impl.user.*;

import java.util.HashMap;
import java.util.Map;
import static controller.manager.PathNameManager.getPathName;

public class CommandsMap {
    public static final Map<String, ICommand> COMMANDS_MAP = new HashMap<>();
    static {
        COMMANDS_MAP.put(getPathName("command.login"), new LoginUserCommand());
        COMMANDS_MAP.put(getPathName("command.logout"), new LogoutUserCommand());
        COMMANDS_MAP.put(getPathName("command.mainPage"), new MainPageCommand());
        COMMANDS_MAP.put(getPathName("command.register"), new RegisterUserCommand());
        COMMANDS_MAP.put(getPathName("command.profile"), new ProfilePageCommand());
        COMMANDS_MAP.put(getPathName("command.saveProfile"), new SaveProfileCommand());
        COMMANDS_MAP.put(getPathName("command.getUserListTable"), new UserListPageCommand());
        COMMANDS_MAP.put(getPathName("command.addUserPage"), new AddUserPageCommand());
        COMMANDS_MAP.put(getPathName("command.setUserStatus"), new SetUserStatusCommand());
        COMMANDS_MAP.put(getPathName("command.changePassword"), new SetUserPasswordCommand());
        COMMANDS_MAP.put(getPathName("command.getServicesListTable"), new ServicesListPageCommand());
        COMMANDS_MAP.put(getPathName("command.getTariffsListTable"), new TariffsListPageCommand());
        COMMANDS_MAP.put(getPathName("command.addServicePage"), new AddServicePageCommand());
        COMMANDS_MAP.put(getPathName("command.createService"), new CreateServiceCommand());

    }
}
