package controller.impl;

import controller.ICommand;
import controller.impl.User.*;

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

    }
}
