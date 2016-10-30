package io.joggr;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class JogEntry {

    @Id
    final private String id;
    final private int distanceMeters;
    final private int timeSeconds;
    final private Date date;

    protected JogEntry() {
        this(0, 0);
    }

    public JogEntry(
            @JsonProperty("id") String id,
            @JsonProperty("distanceMeters") int distanceMeters,
            @JsonProperty("timeSeconds") int timeSeconds,
            @JsonProperty("date") Date date
    ) {
        this.id = id == null ? UUID.randomUUID().toString() : UUID.fromString(id).toString();
        this.distanceMeters = distanceMeters;
        this.timeSeconds = timeSeconds;
        this.date = date == null ? new Date() : date;
    }

    public JogEntry(int distanceMeters, int timeSeconds, Date date) {
        this(null, distanceMeters, timeSeconds, date);
    }

    public JogEntry(int distanceMeters, int timeSeconds) {
        this(null, distanceMeters, timeSeconds, null);
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
}
