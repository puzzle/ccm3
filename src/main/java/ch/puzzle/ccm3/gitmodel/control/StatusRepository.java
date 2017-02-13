package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.gitmodel.entity.Branch;
import ch.puzzle.ccm3.gitmodel.entity.Repository;
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

        // TODO: implement status search

        offset = offset == null ? 0 : offset;
        limit = limit == null ? PAGE_SIZE : limit;

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Status> query = createCriteriaQuery();

        // build where clause
        Root<Status> status = query.from(Status.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, builder, status);

        // using branchId or repositoryId if not null
        Join<Status, Branch> branch = status.join("branch");
        if(branchId != null && branchId > 0) {
            predicates.add(builder.equal(branch.get("id"), branchId));
        } else if(repositoryId != null) {
            Join<Branch, Repository> repository = branch.join("repository");
            predicates.add(builder.equal(repository.get("id"), repositoryId));
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

    public long countSearchResults(Map<String, String> searchParameters) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Status> from = query.from(Status.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, builder, from);
        query.where(predicates.toArray(new Predicate[0]));
        query.select(builder.count(from));

        Long count = getEntityManager().createQuery(query).getSingleResult();
        return count != null ? count : 0;
    }
}
