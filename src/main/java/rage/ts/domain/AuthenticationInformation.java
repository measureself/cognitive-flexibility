package rage.ts.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class AuthenticationInformation extends AbstractPersistable<Long> implements Serializable {

    @JoinColumn
    @ManyToOne
    private Participant participant;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date authenticationTime;
    private String metadata;
    @Lob
    private String details;

    public AuthenticationInformation() {
        super();
    }

    public AuthenticationInformation(Participant participant, String details, String metadata) {
        this();
        this.participant = participant;
        this.authenticationTime = new Date();
        this.details = details;
        this.metadata = metadata;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Date getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(Date authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
