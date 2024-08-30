package org.aurora.base.common.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRuleHelper {
    // op
    public static final String CONTAINS = "contains";
    public static final String EQUAL = "equal";
    public static final String NOTEQUAL = "notequal";
    public static final String BEGIN_WITH = "beginwith";
    public static final String END_WITH = "endwith";
    public static final String GREATER_OR_EQUAL = "greaterorequal";
    public static final String LESS_OR_EQUAL = "lessorequal";
    public static final String GREATER = "greater";
    public static final String LESS = "less";
    // type
    public static final String DATE_BOX = "datebox";

    private String field;
    private String op;
    private Object value;
    private String type;
    private Class<?> fieldType;

    public String getOp() {
        return switch (op) {
            case CONTAINS -> "ilike";
            case EQUAL -> "=";
            case NOTEQUAL -> "<>";
            case BEGIN_WITH, GREATER_OR_EQUAL -> ">=";
            case END_WITH, LESS_OR_EQUAL -> "<=";
            case GREATER -> ">";
            case LESS -> "<";
            default -> op;
        };
    }

    public Object getValue() {
        if (CONTAINS.equals(op)) {
            return "%" + value.toString() + "%";
        }
        if (DATE_BOX.equals(type)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(value.toString(), formatter);
            if (fieldType == LocalDateTime.class) {
                return localDate.atStartOfDay();
            }
            return localDate;
        }
        return value;
    }
}
