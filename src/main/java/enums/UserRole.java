package enums;
import static controller.manager.PathNameManager.*;

public enum UserRole {
    ADMIN(getPathName("page.admin")),
    CUSTOMER(getPathName("page.customer")),
    ACCOUNTANT(getPathName("page.customer")),
    UNKNOWN (getPathName("page.login"));
    private final String mainPage;

    UserRole(String mainPage) {
        this.mainPage = mainPage;
    }

    public String getMainPage() {
        return mainPage;
    }
}
