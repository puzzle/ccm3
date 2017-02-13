package ch.puzzle.ccm3.gitmodel.boundary;

import ch.puzzle.ccm3.gitmodel.control.BranchRepository;
import ch.puzzle.ccm3.gitmodel.entity.Branch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(tags = "ccm3-viewer")
@Stateless
@Path("/branches")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BranchResource {

    private final Logger logger = LoggerFactory.getLogger(BranchResource.class);

    @Inject
    private BranchRepository repository;

    @GET
    @Path("/{id}")
    @ApiOperation("Find a branch by id, include statuses")
    public Branch getRepositoryGroupById(@PathParam("id") long id) throws WebApplicationException {

        Branch branch = repository.findByIdWithChilds(id);
        if (repository == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return branch;
    }

}
