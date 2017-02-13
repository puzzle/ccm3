package ch.puzzle.ccm3;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

import static ch.puzzle.ccm3.SortCriteria.SortDirection.ASC;

public abstract class BaseRepository<T> {
    private final Class<T> entityType;
    @PersistenceContext
    private EntityManager entityManager;

    protected BaseRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Creates an entity on the database. The object is updated with the id that it is created with.
     *
     * @param entity The entity to create
     */
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Finds an entity by its id.
     *
     * @param id The id of the entity to look for
     * @return The entity from the database or null if it was not found
     */
    public T find(long id) {
        return entityManager.find(entityType, id);
    }

    /**
     * Returns a type-safe result list which contains all entities of the provided type.
     *
     * @return the type-safe result list.
     */
    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityType);
        CriteriaQuery<T> all = query.select(query.from(entityType));
        return resultList(entityManager.createQuery(all));
    }

    /**
     * Returns a type-safe result list of the given query.
     *
     * @param query the query
     * @return the result list
     */
    protected List<T> resultList(TypedQuery<T> query) {
        return query.getResultList();
    }

    /**
     * Returns a type-safe single result of the given query or null.
     *
     * @param query Typed query to get result from
     * @return the result or null
     * @throws NonUniqueResultException if more than one result
     */
    protected T singleResult(TypedQuery<T> query) {
        List<T> resultList = resultList(query);

        if (resultList.isEmpty()) {
            return null;
        }

        if (resultList.size() > 1) {
            // maybe the result is a join, so make it distinct.
            Set<T> distinctResult = new HashSet<>(resultList);
            if (distinctResult.size() > 1) {
                throw new NonUniqueResultException("Result for query '" + query + "' must contain exactly one item");
            }
        }

        return resultList.get(0);
    }

    protected TypedQuery<T> createNamedQuery(String queryName) {
        return entityManager.createNamedQuery(queryName, entityType);
    }

    protected TypedQuery<T> createNamedQueryWithGraph(String queryName, EntityGraph<T> graph) {
        TypedQuery<T> query = createNamedQuery(queryName);
        query.setHint("javax.persistence.loadgraph", graph);
        return query;
    }

    protected T findWithGraph(EntityGraph<T> graph, long id) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        return entityManager.find(entityType, id, hints);
    }

    protected EntityGraph<T> getGraph() {
        return entityManager.createEntityGraph(entityType);
    }

    protected CriteriaQuery<T> createCriteriaQuery() {
        return getCriteriaBuilder().createQuery(entityType);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected TypedQuery<T> createTypedQuery(CriteriaQuery<T> query) {
        return entityManager.createQuery(query);
    }

    protected String createSearchString(String input) {
        return input.toLowerCase().replaceAll("\\*", "%") + "%";
    }

    protected List<Predicate> buildSearchPredicates(Map<String, String> searchParameters, CriteriaBuilder builder, Root<T> from) {
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
        return predicates;
    }

    protected Order getOrderFor(Root<T> from, CriteriaBuilder builder, SortCriteria sort) {
        try {
            return ASC.equals(sort.getDirection()) ?
                    builder.asc(from.get(sort.getField())) :
                    builder.desc(from.get(sort.getField()));
        } catch (IllegalArgumentException e) {
            // ignore if field was not found on entity
            return null;
        }
    }
}
