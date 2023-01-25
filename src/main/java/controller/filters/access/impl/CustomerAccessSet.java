package controller.filters.access.impl;

import controller.filters.access.IAccessSet;

import java.util.HashSet;
import java.util.Set;

import static controller.manager.PathNameManager.getPathName;

public class CustomerAccessSet implements IAccessSet {
    public static final Set<String> accessSet = new HashSet<>();
    public static final Set<String> commandSet = new HashSet<>();

    static {
        accessSet.add(getPathName("page.login"));
        accessSet.add(getPathName("page.error"));
        accessSet.add(getPathName("page.profile"));
        accessSet.add(getPathName("page.customer"));
        accessSet.add(getPathName("page.controller"));
    }
    static {
        commandSet.add(getPathName("command.login"));
        commandSet.add(getPathName("command.logout"));
        commandSet.add(getPathName("command.mainPage"));
        commandSet.add(getPathName("command.profile"));
        commandSet.add(getPathName("command.saveProfile"));
        commandSet.add(getPathName("command.changePassword"));
        commandSet.add(getPathName("command.getServicesListTable"));
        commandSet.add(getPathName("command.getTariffsListUserTable"));
        commandSet.add(getPathName("command.subscribe"));
        commandSet.add(getPathName("command.unsubscribe"));
        commandSet.add(getPathName("command.downloadPrice"));
        commandSet.add(getPathName("command.addPayment"));
        commandSet.add(getPathName("command.addPaymentPage"));
        commandSet.add(getPathName("command.getPaymentsListUserTable"));
        commandSet.add(getPathName("command.getWithdrawListUserTable"));
    }

    @Override
    public Set<String> getPageAccessSet() {
        return accessSet;
    }
    @Override
    public Set<String> getCommandAccessSet() {
        return commandSet;
    }
}
