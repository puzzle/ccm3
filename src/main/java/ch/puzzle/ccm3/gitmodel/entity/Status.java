package ch.puzzle.ccm3.gitmodel.entity;

import ch.puzzle.ccm3.gitmodel.entity.JsonViews.FromStatus;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonView(FromStatus.class)
    private Branch branch;

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String stage;

    @NotNull
    @NotEmpty
    @Size(max = 64)
    private String version;

    @NotNull
    @Column(insertable = false, updatable = false)
    private LocalDateTime executed;

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String userId;

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String status;

    @Size(max = 24)
    private String auftragNr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getExecuted() {
        return executed;
    }

    public void setExecuted(LocalDateTime executed) {
        this.executed = executed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuftragNr() {
        return auftragNr;
    }

    public void setAuftragNr(String auftragNr) {
        this.auftragNr = auftragNr;
    }
}
