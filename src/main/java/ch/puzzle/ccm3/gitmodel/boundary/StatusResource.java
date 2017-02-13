package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.PaginatedResult;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.gitmodel.control.StatusRepository;
import ch.puzzle.ccm3.gitmodel.entity.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.DefaultValues.PAGE_SIZE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/statuses")
@Api(tags = "ccm3-viewer")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class StatusResource {

    @Inject
    StatusRepository repository;

    private Map<Integer, String> columnMapping = new HashMap<>();

    public StatusResource() {
        columnMapping.put(0, "stage");
        columnMapping.put(1, "version");
        columnMapping.put(2, "userId");
        columnMapping.put(3, "status");
        columnMapping.put(4, "auftragNr");
        columnMapping.put(5, "executed");
    }

    @GET
    @ApiOperation("Find Statuses by Repository or Branch. Supports pagination and ordering")
    public Response getStatusEntries(@QueryParam("branchId") Long branchId,
                                     @QueryParam("repositoryId") Long repoositoryId,
                                     @QueryParam("start") Integer offset,
                                     @QueryParam("length") Integer pageSize,
                                     @QueryParam("order[0][column]") Integer sortColumn,
                                     @QueryParam("order[0][dir]") String sortDirection,
                                     @QueryParam("draw") Integer draw) {

        List<Status> result = repository.search(repoositoryId, branchId, offset, pageSize, new SortCriteria(columnMapping.get(sortColumn), sortDirection), null);
        Long totalCount = repository.countSearchResults(null);

        PaginatedResult<Status> paginatedResult = new PaginatedResult<>(result, totalCount, offset != null ? offset : 0,
                pageSize != null ? pageSize : PAGE_SIZE, draw != null ? draw : 1);

        return Response.ok(paginatedResult).build();
    }
}
