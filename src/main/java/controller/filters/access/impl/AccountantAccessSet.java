package controller.filters.access.impl;

import controller.filters.access.IAccessSet;

import java.util.HashSet;
import java.util.Set;

import static controller.manager.PathNameManager.getPathName;

public class AccountantAccessSet implements IAccessSet {
    public static final Set<String> accessSet = new HashSet<>();
    public static final Set<String> commandSet = new HashSet<>();

    static {
        accessSet.add(getPathName("page.main"));
        accessSet.add(getPathName("page.login"));
        accessSet.add(getPathName("page.error"));
        accessSet.add(getPathName("page.profile"));
        accessSet.add(getPathName("page.accountant"));
        accessSet.add(getPathName("page.controller"));
    }

    static {
        commandSet.add(getPathName("command.login"));
        commandSet.add(getPathName("command.logout"));
        commandSet.add(getPathName("command.mainPage"));
        commandSet.add(getPathName("command.profile"));
        commandSet.add(getPathName("command.saveProfile"));
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
