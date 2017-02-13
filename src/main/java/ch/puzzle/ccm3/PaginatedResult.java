package ch.puzzle.ccm3;


import java.util.Collections;
import java.util.List;

/**
 * This class represents a data of a paginated data list.
 *
 * @param <T> the type of the resulting class.
 */
public class PaginatedResult<T> {
    private List<T> data;
    private int draw;
    /**
     * Total results found.
     */
    private int recordsTotal;
    private int recordsFiltered;
    /**
     * Offset used for querying the data.
     */
    private int offset;
    /**
     * Page size (limit) used for querying the data.
     */
    private int pageSize;

    public PaginatedResult(int resultSize, int offset, int pageSize) {
        this(Collections.emptyList(), resultSize, offset, pageSize);
    }

    public PaginatedResult(List<T> data, int recordsTotal, int offset, int pageSize) {
        this.data = Collections.unmodifiableList(data);
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsTotal;
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public PaginatedResult(List<T> data, int recordsTotal, int offset, int pageSize, int draw) {
        this.data = Collections.unmodifiableList(data);
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsTotal;
        this.offset = offset;
        this.pageSize = pageSize;
        this.draw = draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getDraw() {
        return draw;
    }

    public List<T> getData() {
        return data;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getOffset() {
        return offset;
    }
}
