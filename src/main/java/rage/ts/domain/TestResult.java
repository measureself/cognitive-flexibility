package rage.ts.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TestResult extends AbstractPersistable<Long> implements Serializable {

    @JoinColumn(name = "PARTICIPANT_ID")
    @ManyToOne
    private Participant participant;
    private String testType;
    private String info;
    private Long listId;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date testTime;
    private Double initTime;
    private Double testStartTime;
    private Double testEndTime;
    @JoinColumn(nullable = true, name = "TESTRESULT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<AdditionalKeyPress> additionalKeyPresses;
    @JoinColumn(nullable = true, name = "TESTRESULT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reaction> reactions;

    public TestResult() {
        testTime = new Date();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public Date getTimestamp() {
        return testTime;
    }

    public void setTimestamp(Date timestamp) {
        this.testTime = timestamp;
    }

    public Double getInitTime() {
        return initTime;
    }

    public void setInitTime(Double initTime) {
        this.initTime = initTime;
    }

    public Double getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(Double testStartTime) {
        this.testStartTime = testStartTime;
    }

    public Double getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(Double testEndTime) {
        this.testEndTime = testEndTime;
    }

    public List<AdditionalKeyPress> getAdditionalKeyPresses() {
        return additionalKeyPresses;
    }

    public void setAdditionalKeyPresses(List<AdditionalKeyPress> additionalKeyPresses) {
        this.additionalKeyPresses = additionalKeyPresses;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}
