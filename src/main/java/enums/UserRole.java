package enums;
import controller.filters.access.IAccessSet;
import controller.filters.access.impl.AccountantAccessSet;
import controller.filters.access.impl.AdminAccessSet;
import controller.filters.access.impl.AnonymousAccessSet;
import controller.filters.access.impl.CustomerAccessSet;

import static controller.manager.PathNameManager.*;

public enum UserRole {
    ADMIN(getPathName("page.admin"), getPathName("content.dashboard"), new AdminAccessSet()),
    CUSTOMER(getPathName("page.customer"), getPathName("content.userDashboard"),  new CustomerAccessSet()),
    ACCOUNTANT(getPathName("page.accountant"), getPathName("content.userDashboard"), new AccountantAccessSet()),
    ANONYMOUS (getPathName("page.login"), "",new AnonymousAccessSet());

    private final String mainPage;
    private final String dashboard;
    private final IAccessSet accessSet;

    UserRole(String mainPage, String dashboard, IAccessSet accessSet) {
        this.mainPage = mainPage;
        this.dashboard=dashboard;
        this.accessSet = accessSet;
    }

    public String getMainPage() {
        return mainPage;
    }
    public String getDashboard() {
        return dashboard;
    }

    public boolean isPageAccessAllowed(String page) {
        return this.accessSet.getPageAccessSet().contains(page);
    }
    public boolean isCommandAccessAllowed(String command) {
        return this.accessSet.getCommandAccessSet().contains(command);
    }
}
