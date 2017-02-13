package ch.puzzle.ccm3.gitmodel.control;

import ch.puzzle.ccm3.PersistenceTestRunner;
import ch.puzzle.ccm3.gitmodel.entity.Repository;
import ch.puzzle.ccm3.gitmodel.entity.RepositoryGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(PersistenceTestRunner.class)
public class RepositoryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private RepositoryRepository repository;

    private RepositoryGroupRepository repositoryGroupRepository;
    private RepositoryGroup rg1;
    private RepositoryGroup rg2;

    @Before
    public void init() {
        repository = new RepositoryRepository();
        repository.setEntityManager(entityManager);

        repositoryGroupRepository = new RepositoryGroupRepository();
        repositoryGroupRepository.setEntityManager(entityManager);

        rg1 = createInstance("rg1");
        rg2 = createInstance("rg2");

        repositoryGroupRepository.persist(rg1);
        repositoryGroupRepository.persist(rg2);
    }

    @Test
    public void shouldFindRepositoryEntryBySearchParameters() throws Exception {
        // given
        Repository rep1 = createInstance(rg1);
        Repository rep2 = createInstance(rg1);

        rep1.setName("R1");
        rep2.setName("R2");

        repository.persist(rep1);
        repository.persist(rep2);

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("name", "R1");

        // when
        List<Repository> result = repository.search(searchParameters);

        // then
        assertThat(result.size(), is(1));
        assertThat(result, contains(rep1));
    }

    @Test
    public void shouldFindRepositoryEntriesBySearchParameters() throws Exception {
        // given
        Repository rep1 = createInstance(rg1);
        Repository rep2 = createInstance(rg2);

        rep1.setName("R1");
        rep2.setName("R2");

        repository.persist(rep1);
        repository.persist(rep2);

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("name", "R");

        // when
        List<Repository> result = repository.search(searchParameters);

        // then
        assertThat(result.size(), is(2));
        assertThat(result, containsInAnyOrder(rep1, rep2));
    }

    @Test
    public void shouldNotFindRepositoryEntriesBySearchParameters() throws Exception {
        // given
        Repository rep1 = createInstance(rg1);
        Repository rep2 = createInstance(rg2);

        rep1.setName("R1");
        rep2.setName("R2");

        repository.persist(rep1);
        repository.persist(rep2);

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("name", "X");

        // when
        List<Repository> result = repository.search(searchParameters);

        // then
        assertThat(result.size(), is(0));
    }

    private Repository createInstance(RepositoryGroup repositoryGroup) {
        return new Repository(repositoryGroup, "R", null);
    }

    private RepositoryGroup createInstance(String name) {
        RepositoryGroup repositoryGroup = new RepositoryGroup();
        repositoryGroup.setName(name);
        return repositoryGroup;
    }
}
