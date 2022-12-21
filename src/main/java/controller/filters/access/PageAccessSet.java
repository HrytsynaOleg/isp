package controller.filters.access;

import java.util.HashSet;
import java.util.Set;

import static controller.manager.PathNameManager.*;

public class PageAccessSet {
    public static final Set<String> UNKNOWN_USER_PAGES = new HashSet<>();
    public static final Set<String> LOGGED_USER_PAGES = new HashSet<>();
    public static final Set<String> ADMIN_USER_PAGES = new HashSet<>();
    public static final Set<String> CUSTOMER_USER_PAGES = new HashSet<>();
    public static final Set<String> ACCOUNTANT_USER_PAGES = new HashSet<>();

    public PageAccessSet() {
    }

    static {
        UNKNOWN_USER_PAGES.add(getPathName("page.main"));
        UNKNOWN_USER_PAGES.add(getPathName("page.login"));
        UNKNOWN_USER_PAGES.add(getPathName("page.register"));
        UNKNOWN_USER_PAGES.add(getPathName("page.error"));
    }
    static {
        LOGGED_USER_PAGES.addAll(UNKNOWN_USER_PAGES);
        LOGGED_USER_PAGES.add(getPathName("page.profile"));
    }
    static {
        ADMIN_USER_PAGES.addAll(LOGGED_USER_PAGES);
        ADMIN_USER_PAGES.add(getPathName("page.admin"));
    }
    static {
        CUSTOMER_USER_PAGES.addAll(LOGGED_USER_PAGES);
        CUSTOMER_USER_PAGES.add(getPathName("page.customer"));
    }
    static {
        ACCOUNTANT_USER_PAGES.addAll(LOGGED_USER_PAGES);
        ACCOUNTANT_USER_PAGES.add(getPathName("page.accountant"));
    }

}
