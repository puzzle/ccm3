package ch.puzzle.ccm3.gitmodel.entity;

import ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromBranch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"repository_id", "name"}))
public class Branch implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference("Branch-Repository")
    private Repository repository;

    @NotNull
    @NotEmpty
    @Size(max = 128)
    private String name;

    @NotNull
    @Column(insertable = false, updatable = false)
    private LocalDateTime created;

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String creator;

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String status;

    @OneToMany(mappedBy = "branch")
    @OrderBy("executed desc")
    @JsonView(FromBranch.class)
    private List<Status> statuses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
