package ch.puzzle.ccm3.gitmodel.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromRepository;
import static ch.puzzle.ccm3.gitmodel.entity.Repository.FIND_ALL;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = FIND_ALL, query = "SELECT r FROM Repository r ORDER BY r.name ASC")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Repository implements Serializable {

    public static final String FIND_ALL = "Repository.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonView(FromRepository.class)
    private RepositoryGroup repositoryGroup;

    @NotNull
    @NotEmpty
    @Size(max = 128)
    private String name;

    @JsonManagedReference("Branch-Repository")
    @OneToMany(mappedBy = "repository")
    @OrderBy("name")
    private List<Branch> branches;

    public Repository(RepositoryGroup repositoryGroup, String name, List<Branch> branches) {
        this.repositoryGroup = repositoryGroup;
        this.name = name;
        this.branches = branches;
    }

    public Repository() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RepositoryGroup getRepositoryGroup() {
        return repositoryGroup;
    }

    public void setRepositoryGroup(RepositoryGroup repositoryGroup) {
        this.repositoryGroup = repositoryGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

}
