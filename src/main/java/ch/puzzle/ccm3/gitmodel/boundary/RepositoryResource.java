package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.PaginatedResult;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.gitmodel.control.RepositoryRepository;
import ch.puzzle.ccm3.gitmodel.entity.JsonViews;
import ch.puzzle.ccm3.gitmodel.entity.Repository;
import com.fasterxml.jackson.annotation.JsonView;
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
@Path("/repositories")
@Api(tags = "ccm3-viewer")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class RepositoryResource {

    @Inject
    private RepositoryRepository repository;

    @GET
    @Path("/{id}")
    @JsonView(JsonViews.FromRepository.class)
    @ApiOperation("Find a repository by id including its branches")
    public Repository getRepositoryById(@PathParam("id") long id) throws WebApplicationException {
        Repository repository = this.repository.findByIdWithChilds(id);
        if (repository == null) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        return repository;
    }

    @GET
    @ApiOperation("Find Repositsories by name. Supports pagination.")
    public Response findRepositoryEntries(@QueryParam("name") String name,
                                          @QueryParam("offset") Integer offset,
                                          @QueryParam("pageSize") Integer pageSize) {

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("name", name);
        List<Repository> result = repository.search(null, null, new SortCriteria("name", SortCriteria.SortDirection.ASC), searchParameters);

        int totalCount = 123;
        PaginatedResult<Repository> paginatedResult = new PaginatedResult<>(result, totalCount,
                offset != null ? offset : 0, pageSize != null ? pageSize : PAGE_SIZE);

        return Response.ok(paginatedResult).build();
    }
}
