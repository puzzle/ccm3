package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.gitmodel.entity.Status;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.DefaultValues.PAGE_SIZE;
import static ch.puzzle.ccm3.SortCriteria.SortDirection.DESC;

@ApplicationScoped
public class StatusRepository extends BaseRepository<Status> {
    protected StatusRepository() {
        super(Status.class);
    }

    public List<Status> search(Long repositoryId, Long branchId, Integer offset, Integer limit, SortCriteria sort, Map<String, String> searchParameters) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? PAGE_SIZE : limit;

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Status> query = createCriteriaQuery();

        // build where clause
        Root<Status> status = query.from(Status.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, builder, status);
        Predicate parentPredicate = buildRepositoryAndBranchPredicate(repositoryId, branchId, builder, status);
        if (parentPredicate != null) {
            predicates.add(parentPredicate);
        }
        query.where(predicates.toArray(new Predicate[0]));

        // apply order
        sort = ((sort != null) && (sort.getField() != null)) ? sort : new SortCriteria("executed", DESC);
        Order order = getOrderFor(status, builder, sort);
        if (order != null) {
            query.orderBy(order);
        }

        // apply offset and limits
        return resultList(createTypedQuery(query).setFirstResult(offset).setMaxResults(limit));
    }

    public long countSearchResults(Long repositoryId, Long branchId, Map<String, String> searchParameters) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Status> status = query.from(Status.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, builder, status);
        Predicate parentPredicate = buildRepositoryAndBranchPredicate(repositoryId, branchId, builder, status);
        if (parentPredicate != null) {
            predicates.add(parentPredicate);
        }
        query.where(predicates.toArray(new Predicate[0]));
        query.select(builder.count(status));

        Long count = getEntityManager().createQuery(query).getSingleResult();
        return count != null ? count : 0;
    }

    protected Predicate buildRepositoryAndBranchPredicate(Long repositoryId, Long branchId, CriteriaBuilder builder, Root<Status> status) {
        if (branchId != null && branchId > 0) {
            return builder.equal(status.get("branch").get("id"), branchId);
        } else if (repositoryId != null) {
            return builder.equal(status.get("branch").get("repository").get("id"), repositoryId);
        }
        return null;
    }
}
