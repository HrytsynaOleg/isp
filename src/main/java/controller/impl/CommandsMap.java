package controller.impl;

import controller.ICommand;
import controller.impl.User.LogoutUserCommand;
import controller.impl.User.ValidateUserCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandsMap {
    public static final Map<String, ICommand> COMMANDS_MAP = new HashMap<>();
    static {
        COMMANDS_MAP.put("validateUser", new ValidateUserCommand());
        COMMANDS_MAP.put("logoutUser", new LogoutUserCommand());
    }
}
