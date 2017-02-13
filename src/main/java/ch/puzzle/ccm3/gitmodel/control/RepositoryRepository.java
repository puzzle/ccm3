package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.gitmodel.entity.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.DefaultValues.PAGE_SIZE;
import static ch.puzzle.ccm3.SortCriteria.SortDirection.ASC;

@ApplicationScoped
public class RepositoryRepository extends BaseRepository<Repository> {

    protected RepositoryRepository() {
        super(Repository.class);
    }

    public List<Repository> findAllOrderedByName() {
        TypedQuery<Repository> query = createNamedQuery(Repository.FIND_ALL);
        List<Repository> repositories = query.getResultList();
        return repositories;
    }

    public Repository findByIdWithChilds(Long id)  {
        EntityGraph<Repository> graph = getGraph();
        graph.addSubgraph("branches");
        return findWithGraph(graph, id);
    }

    public List<Repository> search(Map<String, String> searchParameters) {
        return search(null, null, null, searchParameters);
    }

    public List<Repository> search(Integer offset, Integer limit, SortCriteria sort, Map<String, String> searchParameters) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? PAGE_SIZE : limit;

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Repository> query = createCriteriaQuery();

        Root<Repository> from = query.from(Repository.class);
        for (Map.Entry<String, String> searchParam : searchParameters.entrySet()) {
            query.where(builder.like(from.get(searchParam.getKey()), createSearchString(searchParam.getValue())));
        }

        // apply order
        if (sort != null && sort.getField() != null) {
            try {
                Order order = ASC.equals(sort.getDirection()) ?
                        builder.asc(from.get(sort.getField())) :
                        builder.desc(from.get(sort.getField()));
                query.orderBy(order);
            } catch (IllegalArgumentException e) {
                // ignore if field was not found on entity
            }
        }

        // apply offset and limits
        return resultList(createTypedQuery(query).setFirstResult(offset).setMaxResults(limit));
    }

    protected String createSearchString(String input) {
        if(input != null){
            return input.replaceAll("\\*", "%") + "%";
        }
        return "%";
    }
}
