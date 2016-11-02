package io.joggr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

@Entity
public class JogEntry {

    @Id
    @Column(unique = true)
    final private String id;
    final private int distanceMeters;
    final private int timeSeconds;
    final private Date date;
    private String userName;

    public @Version Long version;
    public @LastModifiedDate Date updated;

    protected JogEntry() {
        this(0, 0);
    }

    public JogEntry(
            @JsonProperty("id") String id,
            @JsonProperty("distanceMeters") int distanceMeters,
            @JsonProperty("timeSeconds") int timeSeconds,
            @JsonProperty("date") Date date,
            @JsonProperty("username") String userName
    ) {
        this.id = id == null ? UUID.randomUUID().toString() : UUID.fromString(id).toString();
        this.distanceMeters = distanceMeters;
        this.timeSeconds = timeSeconds;
        this.date = date == null ? new Date() : date;
        this.userName = userName;
        this.updated = new Date();
        this.version = 0l;
    }

    public JogEntry(int distanceMeters, int timeSeconds, Date date) {
        this(null, distanceMeters, timeSeconds, date, null);
    }

    public JogEntry(int distanceMeters, int timeSeconds) {
        this(null, distanceMeters, timeSeconds, null, null);
    }

    public String getId() {
        return id;
    }

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public Date getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
