package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.gitmodel.entity.Branch;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityGraph;

@ApplicationScoped
public class BranchRepository extends BaseRepository<Branch> {

    protected BranchRepository() {
        super(Branch.class);
    }

    public Branch findByIdWithChilds(Long id) {
        EntityGraph<Branch> graph = getGraph();
        graph.addSubgraph("statuses");
        return findWithGraph(graph, id);
    }
}

