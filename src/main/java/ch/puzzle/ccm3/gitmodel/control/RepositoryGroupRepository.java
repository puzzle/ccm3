package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.gitmodel.entity.RepositoryGroup;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class RepositoryGroupRepository extends BaseRepository<RepositoryGroup> {

    protected RepositoryGroupRepository() {
        super(RepositoryGroup.class);
    }

    public List<RepositoryGroup> findAllOrderedByName() {
        TypedQuery<RepositoryGroup> query = createNamedQuery(RepositoryGroup.FIND_ALL);
        List<RepositoryGroup> repositoryGroups = query.getResultList();
        return repositoryGroups;
    }

    public RepositoryGroup findByIdWithRepositories(Long id)  {
        EntityGraph<RepositoryGroup> graph = getGraph();
        graph.addSubgraph("repositories");
        return findWithGraph(graph, id);
    }

    public List<RepositoryGroup> findByRepositoryNameWithRepositories(String name) {
        TypedQuery<RepositoryGroup> query = createNamedQuery(RepositoryGroup.FIND_ALL_BY_REPOSITORY_NAME);
        query.setParameter("repositoryName", createSearchString(name));

        List<RepositoryGroup> repositoryGroups = query.getResultList();
        return repositoryGroups;
    }

    private String createSearchString(String input) {
        input  = input != null ? input : "";
        return input.toLowerCase().replaceAll("\\*", "%") + "%";
    }
}
