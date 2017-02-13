package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.gitmodel.control.BranchRepository;
import ch.puzzle.ccm3.gitmodel.entity.Branch;
import ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromBranch;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/branches")
@Api(tags = "ccm3-viewer")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class BranchResource {

    @Inject
    private BranchRepository repository;

    @GET
    @Path("/{id}")
    @JsonView(FromBranch.class)
    @ApiOperation("Find a branch by id, include statuses")
    public Branch getRepositoryGroupById(@PathParam("id") long id) throws WebApplicationException {
        Branch branch = repository.findByIdWithChilds(id);
        if (repository == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return branch;
    }
}
