package controller.filters.access.impl;

import controller.filters.access.IAccessSet;
import java.util.HashSet;
import java.util.Set;

import static controller.manager.PathNameManager.getPathName;

public class AdminAccessSet implements IAccessSet {
    public static final Set<String> pagesSet = new HashSet<>();
    public static final Set<String> commandSet = new HashSet<>();

    static {
        pagesSet.add(getPathName("page.main"));
        pagesSet.add(getPathName("page.login"));
        pagesSet.add(getPathName("page.error"));
        pagesSet.add(getPathName("page.profile"));
        pagesSet.add(getPathName("page.admin"));
        pagesSet.add(getPathName("page.controller"));
    }
    static {
        commandSet.add(getPathName("command.login"));
        commandSet.add(getPathName("command.logout"));
        commandSet.add(getPathName("command.mainPage"));
        commandSet.add(getPathName("command.profile"));
        commandSet.add(getPathName("command.saveProfile"));
        commandSet.add(getPathName("command.getUserListTable"));
        commandSet.add(getPathName("command.addUserPage"));
        commandSet.add(getPathName("command.register"));
        commandSet.add(getPathName("command.changePassword"));
        commandSet.add(getPathName("command.getServicesListTable"));
        commandSet.add(getPathName("command.getTariffsListTable"));
        commandSet.add(getPathName("command.addServicePage"));
        commandSet.add(getPathName("command.createService"));
        commandSet.add(getPathName("command.deleteService"));
        commandSet.add(getPathName("command.editService"));
        commandSet.add(getPathName("command.editServicePage"));
        commandSet.add(getPathName("command.addTariffPage"));
        commandSet.add(getPathName("command.createTariff"));
        commandSet.add(getPathName("command.deleteTariff"));
        commandSet.add(getPathName("command.editTariff"));
        commandSet.add(getPathName("command.editTariffPage"));
        commandSet.add(getPathName("command.setUserBlocked"));
        commandSet.add(getPathName("command.setUserActive"));
    }

    @Override
    public Set<String> getPageAccessSet() {
        return pagesSet;
    }

    @Override
    public Set<String> getCommandAccessSet() {
        return commandSet;
    }
}
