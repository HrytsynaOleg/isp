package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICommand {
    String process(HttpServletRequest request, HttpServletResponse response);
}
