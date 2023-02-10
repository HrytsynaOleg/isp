package controller;

import controller.impl.finance.AddPaymentCommand;
import controller.impl.finance.AddPaymentPageCommand;
import controller.impl.service.*;
import controller.impl.table.*;
import controller.impl.tariff.*;
import controller.impl.user.*;
import service.PriceService;
import service.impl.DtoTablesService;

import java.util.HashMap;
import java.util.Map;

import static dependecies.DependencyManager.*;
import static settings.properties.PathNameManager.getPathName;

public class CommandsMap {
    public static final Map<String, ICommand> COMMANDS_MAP = new HashMap<>();

    static {
        COMMANDS_MAP.put(getPathName("command.login"), new LoginUserCommand(userService));
        COMMANDS_MAP.put(getPathName("command.logout"), new LogoutUserCommand());
        COMMANDS_MAP.put(getPathName("command.mainPage"), new MainPageCommand(tariffService, userService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.register"), new RegisterUserCommand(userService));
        COMMANDS_MAP.put(getPathName("command.profile"), new ProfilePageCommand());
        COMMANDS_MAP.put(getPathName("command.saveProfile"), new SaveProfileCommand(userService));
        COMMANDS_MAP.put(getPathName("command.getUserListTable"), new UserListPageCommand(userService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.addUserPage"), new AddUserPageCommand());
        COMMANDS_MAP.put(getPathName("command.changePassword"), new SetUserPasswordCommand(userService));
        COMMANDS_MAP.put(getPathName("command.getServicesListTable"), new ServicesListPageCommand(serviceService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.getTariffsListTable"), new TariffsListPageCommand(tariffService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.addServicePage"), new AddServicePageCommand());
        COMMANDS_MAP.put(getPathName("command.createService"), new CreateServiceCommand(serviceService));
        COMMANDS_MAP.put(getPathName("command.deleteService"), new DeleteServiceCommand(serviceService));
        COMMANDS_MAP.put(getPathName("command.editService"), new EditServiceCommand(serviceService));
        COMMANDS_MAP.put(getPathName("command.editServicePage"), new EditServicePageCommand(serviceService));
        COMMANDS_MAP.put(getPathName("command.createTariff"), new CreateTariffCommand(tariffService));
        COMMANDS_MAP.put(getPathName("command.addTariffPage"), new AddTariffPageCommand(serviceService));
        COMMANDS_MAP.put(getPathName("command.deleteTariff"), new DeleteTariffCommand(tariffService));
        COMMANDS_MAP.put(getPathName("command.editTariff"), new EditTariffCommand(tariffService));
        COMMANDS_MAP.put(getPathName("command.editTariffPage"), new EditTariffPageCommand(tariffService, serviceService));
        COMMANDS_MAP.put(getPathName("command.getTariffsListUserTable"), new TariffsListUserPageCommand(tariffService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.subscribe"), new SubscribeTariffCommand(tariffService));
        COMMANDS_MAP.put(getPathName("command.unsubscribe"), new UnsubscribeTariffCommand(tariffService));
        COMMANDS_MAP.put(getPathName("command.downloadPrice"), new DownloadTariffCommand(tariffService, PriceService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.addPayment"), new AddPaymentCommand(paymentService));
        COMMANDS_MAP.put(getPathName("command.addPaymentPage"), new AddPaymentPageCommand());
        COMMANDS_MAP.put(getPathName("command.getPaymentsListUserTable"), new PaymentsListUserPageCommand(paymentService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.getWithdrawListUserTable"), new WithdrawListUserPageCommand(paymentService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.setUserBlocked"), new BlockUserCommand(userService));
        COMMANDS_MAP.put(getPathName("command.setUserActive"), new UnblockUserCommand(userService));
        COMMANDS_MAP.put(getPathName("command.getPaymentsListAdminTable"), new PaymentsListAdminPageCommand(paymentService, DtoTablesService.getInstance()));
        COMMANDS_MAP.put(getPathName("command.getWithdrawListAdminTable"), new WithdrawListAdminPageCommand(paymentService, DtoTablesService.getInstance()));
    }
}
