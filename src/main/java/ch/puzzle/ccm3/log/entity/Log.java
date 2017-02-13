package ch.puzzle.ccm3.log.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Entity
@XmlRootElement
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 32)
    private String action;

    @NotNull
    @Size(max = 24)
    private String stage;

    @NotNull
    @Size(max = 128)
    private String repositoryName;
    @NotNull
    @Size(max = 32)
    private String repositoryGroupName;

    @Column(name = "repository_id")
    private Long repositoryId;

    @NotNull
    @Size(max = 128)
    private String branch;

    @NotNull
    @Size(max = 64)
    private String version;

    @NotNull
    @Size(max = 24)
    private String userId;

    private LocalDateTime logdate;

    @Size(max = 24)
    private String auftragNr;

    public  Log() {
    }

    public Log(String repositoryGroupName, String repositoryName, String action, String stage, String branch, String version, String userId, String auftragNr, LocalDateTime logdate) {
        this.action = action;
        this.stage = stage;
        this.repositoryName = repositoryName;
        this.repositoryGroupName = repositoryGroupName;
        this.branch = branch;
        this.version = version;
        this.userId = userId;
        this.logdate = logdate;
        this.auftragNr = auftragNr;
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryGroupName() {
        return repositoryGroupName;
    }

    public void setRepositoryGroupName(String repositoryGroupName) {
        this.repositoryGroupName = repositoryGroupName;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getLogdate() {
        return logdate;
    }

    public void setLogdate(LocalDateTime logdate) {
        this.logdate = logdate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuftragNr() {
        return auftragNr;
    }

    public void setAuftragNr(String auftragNr) {
        this.auftragNr = auftragNr;
    }
}
