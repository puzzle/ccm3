package ch.puzzle.ccm3.log.boundary;

import ch.puzzle.ccm3.PaginatedResult;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.log.control.LogRepository;
import ch.puzzle.ccm3.log.entity.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.DefaultValues.PAGE_SIZE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

@Stateless
@Path("/logs")
@Api(tags = "ccm3-viewer")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class LogResource {

    @Inject
    private LogRepository repository;
    private Map<Integer, String> columnMapping = new HashMap<>();

    public LogResource() {
        columnMapping.put(0, "action");
        columnMapping.put(1, "stage");
        columnMapping.put(2, "repositoryName");
        columnMapping.put(3, "repositoryGroupName");
        columnMapping.put(4, "branch");
        columnMapping.put(5, "version");
        columnMapping.put(6, "userId");
        columnMapping.put(7, "auftragNr");
        columnMapping.put(8, "logdate");
    }

    @GET
    @ApiOperation("Find Logs by filter parameters, supports pagination and ordering")
    public Response getLogEntries(@QueryParam("repositoryName") String repositoryName,
                                  @QueryParam("repositoryGroupName") String repositoryGroupName,
                                  @QueryParam("action") String action,
                                  @QueryParam("stage") String stage,
                                  @QueryParam("branch") String branch,
                                  @QueryParam("version") String version,
                                  @QueryParam("user") String user,
                                  @QueryParam("auftrag") String auftrag,
                                  @QueryParam("lowerDate") LocalDateTime lowerDate,
                                  @QueryParam("upperDate") LocalDateTime upperDate,
                                  @QueryParam("start") Integer offset,
                                  @QueryParam("length") Integer pageSize,
                                  @QueryParam("order[0][column]") Integer sortColumn,
                                  @QueryParam("order[0][dir]") String sortDirection,
                                  @QueryParam("draw") Integer draw) {

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("action", action);
        searchParameters.put("stage", stage);
        searchParameters.put("repositoryName", repositoryName);
        searchParameters.put("repositoryGroupName", repositoryGroupName);
        searchParameters.put("branch", branch);
        searchParameters.put("version", version);
        searchParameters.put("userId", user);
        searchParameters.put("auftragNr", auftrag);

        List<Log> result = repository.search(offset, pageSize, new SortCriteria(columnMapping.get(sortColumn), sortDirection), lowerDate, upperDate, searchParameters);
        long totalCount = repository.countSearchResults(lowerDate, upperDate, searchParameters);

        PaginatedResult<Log> paginatedResult = new PaginatedResult<>(result, totalCount, offset != null ? offset : 0,
                pageSize != null ? pageSize : PAGE_SIZE, draw != null ? draw : 1);

        return Response.ok(paginatedResult).build();
    }

    @GET
    @Path("/stages")
    @ApiOperation("Finds distinct Stages of all Log records")
    public Response findAllStages() {
        List<DropdownValue> result = new ArrayList<>();
        repository.findAllStages().stream().forEach(action -> result.add(new DropdownValue(action)));
        return ok(result).build();
    }

    @GET
    @Path("/actions")
    @ApiOperation("Finds distinct Actions of all Log records")
    public Response findAllActions() {
        List<DropdownValue> result = new ArrayList<>();
        repository.findAllActions().stream().forEach(action -> result.add(new DropdownValue(action)));
        return ok(result).build();
    }

    public static class DropdownValue {
        private String name;

        public DropdownValue(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
