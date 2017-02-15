package ch.puzzle.ccm3.log.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.log.entity.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.DefaultValues.PAGE_SIZE;
import static ch.puzzle.ccm3.SortCriteria.SortDirection.DESC;
import static ch.puzzle.ccm3.log.entity.Log.FIND_ALL_ACTIONS;
import static ch.puzzle.ccm3.log.entity.Log.FIND_ALL_STAGES;

@ApplicationScoped
public class LogRepository extends BaseRepository<Log> {
    protected LogRepository() {
        super(Log.class);
    }

    public List<String> findAllStages() {
        return getEntityManager().createNamedQuery(FIND_ALL_STAGES, String.class).getResultList();
    }

    public List<String> findAllActions() {
        return getEntityManager().createNamedQuery(FIND_ALL_ACTIONS, String.class).getResultList();
    }

    public List<Log> search(Map<String, String> searchParameters) {
        return search(null, null, null, null, null, searchParameters);
    }

    public List<Log> search(LocalDateTime lowerDate, LocalDateTime upperDate) {
        return search(null, null, null, lowerDate, upperDate, null);
    }

    public List<Log> search(Integer offset, Integer limit, SortCriteria sort, LocalDateTime lowerDate, LocalDateTime upperDate, Map<String, String> searchParameters) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? PAGE_SIZE : limit;

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Log> query = createCriteriaQuery();

        // build where clause
        Root<Log> from = query.from(Log.class);
        List<Predicate> predicates = buildSearchPredicatesWithDateRange(searchParameters, lowerDate, upperDate, builder, from);
        query.where(predicates.toArray(new Predicate[0]));

        // apply order
        sort = ((sort != null) && (sort.getField() != null)) ? sort : new SortCriteria("logdate", DESC);
        Order order = getOrderFor(from, builder, sort);
        if (order != null) {
            query.orderBy(order);
        }

        // apply offset and limits
        return resultList(createTypedQuery(query).setFirstResult(offset).setMaxResults(limit));
    }


    public long countSearchResults(LocalDateTime lowerDate, LocalDateTime upperDate, Map<String, String> searchParameters) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Log> from = query.from(Log.class);
        List<Predicate> predicates = buildSearchPredicatesWithDateRange(searchParameters, lowerDate, upperDate, builder, from);
        query.where(predicates.toArray(new Predicate[0]));
        query.select(builder.count(from));

        Long count = getEntityManager().createQuery(query).getSingleResult();
        return count != null ? count : 0;
    }

    private List<Predicate> buildSearchPredicatesWithDateRange(Map<String, String> searchParameters, LocalDateTime lowerDate, LocalDateTime upperDate, CriteriaBuilder builder, Root<Log> from) {
        List<Predicate> predicates = super.buildSearchPredicates(searchParameters, builder, from);
        Predicate datePredicate = builder.between(from.get("logdate"),
                lowerDate != null ? lowerDate : LocalDateTime.of(1900, 1, 1, 0, 0),
                upperDate != null ? upperDate : LocalDateTime.of(2100, 1, 1, 0, 0));
        predicates.add(datePredicate);
        return predicates;
    }
}
