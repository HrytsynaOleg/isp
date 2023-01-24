package dao;

import exceptions.IncorrectFormatException;
import service.IValidatorService;
import service.impl.ValidatorService;

import java.util.Map;

public class QueryBuilder {
    private final StringBuilder builder;
    private final Map<String, String> parameters;
    private final IValidatorService validator = new ValidatorService();

    public QueryBuilder(String query, Map<String, String> parameters) {
        this.builder = new StringBuilder(query);
        this.parameters = parameters;
    }

    private void addSort() {
        if (parameters==null) return;
        try {
            String column = parameters.get("sortColumn");
            String order = parameters.get("order");
            validator.validateEmptyString(column, "");
            validator.validateEmptyString(order, "");

            this.builder
                    .append(" ORDER BY ")
                    .append(column)
                    .append(" ")
                    .append(order);

        } catch (NullPointerException | IncorrectFormatException ignored) {
        }
    }

    private void addLimit() {
        if (parameters==null) return;
        try {
            String limit = parameters.get("limit");
            String total = parameters.get("total");
            validator.validateEmptyString(limit, "");
            validator.validateEmptyString(total, "");
            this.builder
                    .append(" LIMIT ")
                    .append(limit)
                    .append(",")
                    .append(total);
        } catch (NullPointerException | IncorrectFormatException ignored) {
        }
    }

    private void addSearch() {
        if (parameters==null) return;
        try {
            String column = parameters.get("searchColumn");
            String criteria = parameters.get("criteria");
            validator.validateEmptyString(column, "");
            validator.validateEmptyString(criteria, "");

            this.builder.append(this.builder.toString().contains("WHERE")?" AND ":" WHERE ");

            this.builder
                    .append(column)
                    .append(" LIKE '%")
                    .append(criteria)
                    .append("%'");
        } catch (NullPointerException | IncorrectFormatException ignored) {
        }
    }

    public String build() {
        this.addSearch();
        this.addSort();
        this.addLimit();
        return this.builder.toString();
    }

    public String buildOnlySearch() {
        this.addSearch();
        return this.builder.toString();
    }
}
