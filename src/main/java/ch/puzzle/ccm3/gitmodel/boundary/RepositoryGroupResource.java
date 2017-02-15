package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.gitmodel.control.RepositoryGroupRepository;
import ch.puzzle.ccm3.gitmodel.control.RepositoryRepository;
import ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromRepositoryGroup;
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

import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;

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
    @JsonView(FromRepositoryGroup.class)
    @ApiOperation("Find all repository groups entries")
    public Response getRepositoryGroupEntries() {
        List<RepositoryGroup> repositoryGroups = repositoryGroupRepository.findAllOrderedByName();
        return (repositoryGroups == null || repositoryGroups.isEmpty()) ? noContent().build() : ok(repositoryGroups).build();
    }

    @GET
    @Path("/{id}")
    @JsonView(FromRepositoryGroup.class)
    @ApiOperation("Find a repository group by id, include repository")
    public Response getRepositoryGroupById(@PathParam("id") long id) {
        RepositoryGroup repositoryGroup = repositoryGroupRepository.findByIdWithRepositories(id);
        return repositoryGroup == null ? noContent().build() : ok(repositoryGroup).build();
    }


    @GET
    @JsonView(FromRepositoryGroup.class)
    @Path("/repository-name/{repositoryName}")
    @ApiOperation("Find repository group by child Repositories name")
    public Response findRepositoryGroupsWithRepositoryEntriesByRepositoryName(@PathParam("repositoryName") String repositoryName) {
        List<RepositoryGroup> repositoryGroups = repositoryGroupRepository.findByRepositoryNameWithRepositories(repositoryName);
        return (repositoryGroups == null || repositoryGroups.isEmpty()) ? noContent().build() : ok(repositoryGroups).build();
    }
}