package utils;

import entity.ChangeLog;
import entity.LogEntry;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlLogManager {

    private static final String LOG_FILE = "log.xml";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    private static final Logger log = LoggerFactory.getLogger(XmlLogManager.class);

    private XmlLogManager(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    public static synchronized void logAction(String actionDescription) {
        try {
            ChangeLog currentLog = loadLog();

            String timestamp = LocalDateTime.now().format(formatter);
            currentLog.getEntries().add(new LogEntry(actionDescription, timestamp));

            saveLog(currentLog);

        } catch (Exception e) {
            log.error("Greška pri logiranju: {} ", e.getMessage());
        }
    }

    public static synchronized List<LogEntry> getEntries() {
        return loadLog().getEntries();
    }

    /**
     * Marshalling - kvazi serijalizacija
     */
    private static void saveLog(ChangeLog log) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ChangeLog.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(log, new File(LOG_FILE));
    }


    private static ChangeLog loadLog() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            return new ChangeLog(new ArrayList<>());
        }

        try {
            JAXBContext context = JAXBContext.newInstance(ChangeLog.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ChangeLog) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            log.error("Greška pri čitanju loga, kreiram novi. Error: {}",e.getMessage());
            return new ChangeLog(new ArrayList<>());
        }
    }
}
