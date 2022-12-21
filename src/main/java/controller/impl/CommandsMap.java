package controller.impl;

import controller.ICommand;
import controller.impl.User.LogoutUserCommand;
import controller.impl.User.LoginUserCommand;
import controller.impl.User.ToMainPageCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandsMap {
    public static final Map<String, ICommand> COMMANDS_MAP = new HashMap<>();
    static {
        COMMANDS_MAP.put("loginUser", new LoginUserCommand());
        COMMANDS_MAP.put("logoutUser", new LogoutUserCommand());
        COMMANDS_MAP.put("toLoginPage", new ToMainPageCommand());
    }
}
