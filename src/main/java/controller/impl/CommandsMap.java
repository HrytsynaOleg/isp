package controller.impl;

import controller.ICommand;
import controller.impl.User.LogoutUserCommand;
import controller.impl.User.LoginUserCommand;
import controller.impl.User.RegisterUserCommand;
import controller.impl.User.ToMainPageCommand;

import java.util.HashMap;
import java.util.Map;
import static controller.manager.PathNameManager.getPathName;

public class CommandsMap {
    public static final Map<String, ICommand> COMMANDS_MAP = new HashMap<>();
    static {
        COMMANDS_MAP.put(getPathName("command.login"), new LoginUserCommand());
        COMMANDS_MAP.put(getPathName("command.logout"), new LogoutUserCommand());
        COMMANDS_MAP.put(getPathName("command.mainPage"), new ToMainPageCommand());
        COMMANDS_MAP.put(getPathName("command.register"), new RegisterUserCommand());

    }
}
