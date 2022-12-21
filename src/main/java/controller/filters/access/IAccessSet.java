package controller.filters.access;

import java.util.Set;

public interface IAccessSet {
    Set<String> getPageAccessSet();
    Set<String> getCommandAccessSet();
}
