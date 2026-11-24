package entity;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "changeLog")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeLog {

    @XmlElement(name = "entry")
    private List<LogEntry> entries = new ArrayList<>();

    public ChangeLog() {}

    public ChangeLog(List<LogEntry> entries) {
        this.entries = entries;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LogEntry> entries) {
        this.entries = entries;
    }
}
