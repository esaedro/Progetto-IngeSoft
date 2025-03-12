import application.Configuratore;
import application.Session;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class TestMain {


//
//    public static void main(String[] args) {
//        Session session = new Session();
//        Configuratore configuratore = new Configuratore("C_Dilbert", "admin");
//        session.salva();
//    }

    @Test
    void timeTesting() {

        String dateTimeExpected = "Mon Dec 22 11:15:30 CET 2014";

        Clock clock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("GMT"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(clock.instant()));

        assertEquals(calendar.getTime().toString(), dateTimeExpected, "Skibidi toilet");
    }
}