import application.Configuratore;
import application.Session;
import application.Utente;
import application.Volontario;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals(calendar.getTime().toString(), dateTimeExpected, "Cambio di data non funzionante");
    }

    @Test
    void loginConfiguratoreTest() {
        Utente utenteProvvisorio = new Utente(new Session());

        Utente finale = utenteProvvisorio.login("C_Dilbert", "pallos");

        assertInstanceOf(Configuratore.class, finale,"Login configuratore non restituisce i permessi");
    }
}