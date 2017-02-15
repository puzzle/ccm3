package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.gitmodel.control.RepositoryGroupRepository;
import ch.puzzle.ccm3.gitmodel.control.RepositoryRepository;
import ch.puzzle.ccm3.gitmodel.entity.JsonViews;
import ch.puzzle.ccm3.gitmodel.entity.RepositoryGroup;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Api(tags = "ccm3-viewer")
@Path("/repository-groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RepositoryGroupResource {
    @Inject
    private RepositoryGroupRepository repositoryGroupRepository;

    @Inject
    private RepositoryRepository repositoryRepository;

    @GET
    @JsonView(JsonViews.FromRepositoryGroup.class)
    @ApiOperation("Find all repository groups entries")
    public List<RepositoryGroup> getRepositoryGroupEntries() {
        return repositoryGroupRepository.findAllOrderedByName();
    }

    @GET
    @Path("/{id}")
    @JsonView(JsonViews.FromRepositoryGroup.class)
    @ApiOperation("Find a repository group by id, include repository")
    public RepositoryGroup getRepositoryGroupById(@PathParam("id") long id) throws WebApplicationException {

        RepositoryGroup repositoryGroup = repositoryGroupRepository.findByIdWithRepositories(id);
        if (repositoryGroup == null) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        return repositoryGroup;
    }


    @GET
    @Path("/repository-name/{repositoryName}")
    @ApiOperation("Find repository group by child Repositories name")
    public List<RepositoryGroup> findRepositoryGroupsWithRepositoryEntriesByRepositoryName(@PathParam("repositoryName") String repositoryName) {

        List<RepositoryGroup> repositoryGroups = repositoryGroupRepository.findByRepositoryNameWithRepositories(repositoryName);
        if (repositoryGroups == null || repositoryGroups.isEmpty()) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
        return repositoryGroups;
    }
}