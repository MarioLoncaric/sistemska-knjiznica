package entity;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class LogEntry implements Serializable {

    @XmlElement
    private String action;

    @XmlElement
    private String time;

    public LogEntry() {}

    public LogEntry(String action, String timestamp) {
        this.action = action;
        this.time = timestamp;
    }

    public String getAction() { return action; }
    public String getTimestamp() { return time; }

    @Override
    public String toString() {
        return "[" + time + "] " + action;
    }
}
