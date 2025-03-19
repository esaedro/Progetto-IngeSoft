import application.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utility.FileManager;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    @BeforeAll
    static void setup() {
        System.setProperty("fileUtenti", "testUtenti.json");
        System.setProperty("fileVisite", "testVisite.json");
        System.setProperty("fileLuoghi", "testLuoghi.json");
        System.setProperty("fileStorico", "testStorico.json");
        System.setProperty("fileTipoVisite", "testTipoVisite.json");
        System.setProperty("fileParametriGlobali", "testParametriGlobali.json");
    }

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
        ArrayList<Utente> utentiTest = new ArrayList<>();
        utentiTest.add(new Configuratore("C_Dilbert", "admin"));
        utenteProvvisorio.getSession().setUtenti(utentiTest);
        utenteProvvisorio.getSession().salvaUtenti();

        Utente finale = utenteProvvisorio.login("C_Dilbert", "admin");

        assertInstanceOf(Configuratore.class, finale,"Login configuratore non restituisce i permessi");
    }

    @Test
    void backupStorico() {
        Session session = new Session();
        session.setVisite(new ArrayList<>());
        session.getFilemanager().salva(FileManager.fileStorico, null);
        session.addVisita(new Visita("Prova_effettuata", "Bellissima visita", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>(), Calendar.getInstance(),
                StatoVisita.EFFETTUATA, 6));
        session.addVisita(new Visita("Prova_noneffettuata", "Bellissima visita", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>(), Calendar.getInstance(),
                StatoVisita.PROPOSTA, 6));

        session.salva();
        session.carica();

        assertEquals(session.getStoricoVisite().size(), 1, "Problema nel salvataggio dello storico");

    }

    @Test
    void letturaScritturaParametriGlobali() {
        Session session = new Session();
        Luogo.setParametroTerritoriale("ParametroDiTest");
        TipoVisita.setNumeroMassimoIscrittoPerFruitore(20);
        session.salvaParametriGlobali();
        Luogo.setParametroTerritoriale("FakeParametro");
        TipoVisita.setNumeroMassimoIscrittoPerFruitore(10);
        session.caricaParametriGlobali();
        assertEquals(Luogo.getParametroTerritoriale(), "ParametroDiTest", "Errore nella lettura/scritta parametri globali");
        assertEquals(TipoVisita.getNumeroMassimoIscrittoPerFruitore(), 20, "Errore nella lettura/scritta parametri globali");
    }

    @Test
    void letturaScritturaDatePrecluse() {
        Session session = new Session();

        Calendar dataDaInserire = Calendar.getInstance();
        dataDaInserire.set(Calendar.DAY_OF_MONTH, 24);
        dataDaInserire.set(Calendar.MONTH, 0);

        Set<Calendar> testSetCalendar = new HashSet<>();
        testSetCalendar.add(dataDaInserire);
        TipoVisita.aggiungiDatePrecluse(testSetCalendar);

        session.salvaParametriGlobali();
        session.caricaParametriGlobali();

        TipoVisita.getDatePrecluse().forEach((calendar) -> {
            assertTrue(testSetCalendar.add(calendar));
        });
    }
}