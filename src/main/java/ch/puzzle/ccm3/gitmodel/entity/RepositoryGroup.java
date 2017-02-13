package ch.puzzle.ccm3.gitmodel.entity;

import ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromRepositoryGroup;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = RepositoryGroup.FIND_ALL, query = "SELECT rg FROM RepositoryGroup rg ORDER BY rg.name ASC"),
        @NamedQuery(name = RepositoryGroup.FIND_ALL_BY_REPOSITORY_NAME, query = "SELECT distinct rg FROM RepositoryGroup rg JOIN FETCH rg.repositories r WHERE r.name LIKE :repositoryName")
})
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class RepositoryGroup implements Serializable {

    public static final String FIND_ALL = "RepositoryGroup.findAll";
    public static final String FIND_ALL_BY_REPOSITORY_NAME = "RepositoryGroup.findAllByRepositoryName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 32)
    private String name;

    @OrderBy("name")
    @OneToMany(mappedBy = "repositoryGroup")
    @JsonView(FromRepositoryGroup.class)
    private List<Repository> repositories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
