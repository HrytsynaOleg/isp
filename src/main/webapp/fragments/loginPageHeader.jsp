<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <form method="get">
            <div class="mb-3">
                <label>
                    <select class="form-select form-select-sm" aria-label=".form-select-sm example" name="locale" onchange='submit();'>
                        <option value="en" ${sessionScope.locale eq 'en' ? 'selected' : ''}>
                            EN
                        </option>
                        <option value="ua" ${sessionScope.locale eq 'ua' ? 'selected' : ''}>
                            UA
                        </option>
                    </select>
                </label>
                </div>
            </form>
            <hr>