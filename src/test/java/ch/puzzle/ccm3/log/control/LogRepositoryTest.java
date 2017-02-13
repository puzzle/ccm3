package ch.puzzle.ccm3.log.control;

import ch.puzzle.ccm3.PersistenceTestRunner;
import ch.puzzle.ccm3.log.entity.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(PersistenceTestRunner.class)
public class LogRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private LogRepository repository;

    @Before
    public void init() {
        repository = new LogRepository();
        repository.setEntityManager(entityManager);
    }

    @Test
    public void shouldFindLogEntryBySearchParameters() throws Exception {
        // given
        Log log1 = createInstance();
        Log log2 = createInstance();
        log2.setBranch("development");

        repository.persist(log1);
        repository.persist(log2);

        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("repositoryGroupName", "Java Applications");
        searchParameters.put("repositoryName", "ccm3");
        searchParameters.put("action", "Deploy");
        searchParameters.put("stage", "Integration");
        searchParameters.put("branch", "master");
        searchParameters.put("version", "1.0.0-SNAPSHOT");
        searchParameters.put("userId", "tux");
        searchParameters.put("auftragNr", "Auftrag #42");

        // when
        List<Log> result = repository.search(searchParameters);

        // then
        assertThat(result, contains(log1));
    }

    @Test
    public void shouldFilterByDateRange() throws Exception {
        // given
        Log log1 = createInstance();
        Log log2 = createInstance();
        log1.setLogdate(d(2017, 2, 4));
        log2.setLogdate(d(2017, 2, 8));
        repository.persist(log1);
        repository.persist(log2);

        // when & then
        assertThat(repository.search(d(2017, 2, 4), null), containsInAnyOrder(log1, log2));
        assertThat(repository.search(d(2017, 2, 8), null), contains(log2));
        assertThat(repository.search(d(2017, 2, 8), null), contains(log2));
        assertThat(repository.search(d(2017, 2, 8), d(2017, 2, 8)), contains(log2));
        assertThat(repository.search(d(2017, 2, 9), d(2017, 2, 9)), is(empty()));
        assertThat(repository.search(d(2017, 2, 4), d(2017, 2, 4)), contains(log1));
        assertThat(repository.search(null, d(2017, 2, 8)), containsInAnyOrder(log1, log2));
    }

    @Test
    public void shouldFindAllLogEntries() throws Exception {
        // given
        Log log1 = createInstance();
        Log log2 = createInstance();
        log1.setRepositoryName("Repo 1");
        log2.setRepositoryName("Repo 2");
        repository.persist(log1);
        repository.persist(log2);

        // when & then
        assertThat(repository.search(null), containsInAnyOrder(log1, log2));
    }

    private LocalDateTime d(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    private Log createInstance() {
        return new Log("Java Applications", "ccm3", "Deploy", "Integration", "master", "1.0.0-SNAPSHOT", "tux", "Auftrag #42", d(2017, 1, 1));
    }
}
