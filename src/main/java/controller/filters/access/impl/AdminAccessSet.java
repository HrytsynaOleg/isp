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
        pagesSet.add(getPathName("page.register"));
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
