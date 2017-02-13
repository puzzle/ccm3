package ch.puzzle.ccm3.log.control;

import ch.puzzle.ccm3.BaseRepository;
import ch.puzzle.ccm3.SortCriteria;
import ch.puzzle.ccm3.log.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.puzzle.ccm3.SortCriteria.SortDirection.ASC;

@ApplicationScoped
public class LogRepository extends BaseRepository<Log> {
    private final Logger logger = LoggerFactory.getLogger(LogRepository.class);

    protected LogRepository() {
        super(Log.class);
    }

    public List<Log> search(Map<String, String> searchParameters) {
        return search(null, null, null, null, null, searchParameters);
    }

    public List<Log> search(LocalDateTime lowerDate, LocalDateTime upperDate) {
        return search(null, null, null, lowerDate, upperDate, null);
    }

    public List<Log> search(Integer offset, Integer limit, SortCriteria sort, LocalDateTime lowerDate, LocalDateTime upperDate, Map<String, String> searchParameters) {
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 25 : limit;

        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Log> query = createCriteriaQuery();

        // build where clause
        Root<Log> from = query.from(Log.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, lowerDate, upperDate, builder, from);
        query.where(predicates.toArray(new Predicate[0]));

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
        } else {
            query.orderBy(builder.desc(from.get("logdate")));
        }

        // apply offset and limits
        return resultList(createTypedQuery(query).setFirstResult(offset).setMaxResults(limit));
    }

    public long countSearchResults(LocalDateTime lowerDate, LocalDateTime upperDate, Map<String, String> searchParameters) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Log> from = query.from(Log.class);
        List<Predicate> predicates = buildSearchPredicates(searchParameters, lowerDate, upperDate, builder, from);
        query.where(predicates.toArray(new Predicate[0]));
        query.select(builder.count(from));

        Long count = getEntityManager().createQuery(query).getSingleResult();
        return count != null ? count : 0;
    }

    private List<Predicate> buildSearchPredicates(Map<String, String> searchParameters, LocalDateTime lowerDate, LocalDateTime upperDate, CriteriaBuilder builder, Root<Log> from) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchParameters != null) {
            for (Map.Entry<String, String> searchParam : searchParameters.entrySet()) {
                if (searchParam.getKey() == null || searchParam.getValue() == null) {
                    continue;
                }
                try {
                    Path<String> attribute = from.get(searchParam.getKey());
                    Predicate predicate = builder.like(builder.lower(attribute), createSearchString(searchParam.getValue()));
                    predicates.add(predicate);
                } catch (IllegalArgumentException e) {
                    // ignore if field was not found on entity
                }
            }
        }
        Predicate datePredicate = builder.between(from.get("logdate"),
                lowerDate != null ? lowerDate : LocalDateTime.of(1900, 1, 1, 0, 0),
                upperDate != null ? upperDate : LocalDateTime.of(2100, 1, 1, 0, 0));
        predicates.add(datePredicate);
        return predicates;
    }

    private String createSearchString(String input) {
        return input.toLowerCase().replaceAll("\\*", "%") + "%";
    }
}
