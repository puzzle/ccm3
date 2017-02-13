package ch.puzzle.ccm3;

import java.util.Objects;

import static ch.puzzle.ccm3.SortCriteria.SortDirection.ASC;
import static ch.puzzle.ccm3.SortCriteria.SortDirection.DESC;

public class SortCriteria {
    private String field;
    private SortDirection direction = ASC;

    public SortCriteria(String sortString, String direction) {
        if (sortString == null || sortString.isEmpty()) {
            return;
        }
        this.direction = "desc".equalsIgnoreCase(direction) ? DESC : ASC;
        this.field = sortString;
    }

    public SortCriteria(String sortString) {
        if (sortString == null || sortString.isEmpty()) {
            return;
        }
        if (sortString.startsWith("-")) {
            direction = DESC;
            field = sortString.substring(1, sortString.length());
        } else {
            direction = ASC;
            field = sortString;
        }
    }

    public SortCriteria(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SortCriteria that = (SortCriteria) o;
        return Objects.equals(field, that.field) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, direction);
    }

    public enum SortDirection {
        ASC, DESC
    }
}
