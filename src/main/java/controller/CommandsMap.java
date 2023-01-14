package controller;

import controller.ICommand;
import controller.impl.service.*;
import controller.impl.table.ServicesListPageCommand;
import controller.impl.table.TariffsListPageCommand;
import controller.impl.table.TariffsListUserPageCommand;
import controller.impl.table.UserListPageCommand;
import controller.impl.tariff.*;
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
        COMMANDS_MAP.put(getPathName("command.deleteService"), new DeleteServiceCommand());
        COMMANDS_MAP.put(getPathName("command.editService"), new EditServiceCommand());
        COMMANDS_MAP.put(getPathName("command.editServicePage"), new EditServicePageCommand());
        COMMANDS_MAP.put(getPathName("command.createTariff"), new CreateTariffCommand());
        COMMANDS_MAP.put(getPathName("command.addTariffPage"), new AddTariffPageCommand());
        COMMANDS_MAP.put(getPathName("command.deleteTariff"), new DeleteTariffCommand());
        COMMANDS_MAP.put(getPathName("command.editTariff"), new EditTariffCommand());
        COMMANDS_MAP.put(getPathName("command.editTariffPage"), new EditTariffPageCommand());
        COMMANDS_MAP.put(getPathName("command.getTariffsListUserTable"), new TariffsListUserPageCommand());

    }
}
