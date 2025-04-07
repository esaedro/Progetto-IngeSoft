import application.*;
import org.junit.jupiter.api.*;
import utility.FileManager;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMain {

    @BeforeAll
    static void setup() {
        System.setProperty("fileUtenti", "testUtenti.json");
        System.setProperty("fileVisite", "testVisite.json");
        System.setProperty("fileLuoghi", "testLuoghi.json");
        System.setProperty("fileStorico", "testStorico.json");
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
    @Order(1)
    void loginConfiguratoreTest() {
        Session session = new Session();

        Set<Utente> utentiTest = new HashSet<>();
        utentiTest.add(new Configuratore("C_Dilbert", "admin"));
        session.setUtenti(utentiTest);
        session.salvaUtenti();

        Utente finale = session.login("C_Dilbert", "admin");

        assertInstanceOf(Configuratore.class, finale,"Login configuratore non restituisce i permessi");
    }

    @Test
    @Order(2)
    void disponibilitaVolontari() {
        Session session = new Session();
        Set<Integer> disp = new HashSet<>();
        disp.add(3);
        disp.add(4);
        disp.add(8);

        Set<Utente> utentiTest = new HashSet<>();
        utentiTest.add(new Volontario("V_Jhonny", "volontario", disp));

        session.setUtenti(utentiTest);
        session.salvaUtenti();

        Utente finale = session.login("V_Jhonny", "volontario");
        assertInstanceOf(Volontario.class, finale,"Login volontario non restituisce i permessi");

        ((Volontario)finale).getDisponibilita().forEach(giorno ->
                assertFalse(disp.add(giorno), "errore lettura/scrittura disponibilità volontario"));

    }

    @Test
    @Order(3)
    void getDisponibilitaVolontario() {
        Session session = new Session();
        Set<Utente> utentiTest = new HashSet<>();
        utentiTest.add(new Volontario("V_Jhonny", "volontario"));
        session.setUtenti(utentiTest);
        session.salvaUtenti();

        Utente finale = session.login("V_Jhonny", "volontario");

        session.setVisite(new HashSet<>());

        Set<Volontario> volontari = new HashSet<>();
        volontari.add((Volontario) finale);

        TipoVisita associata = new TipoVisita("Jhonny_visita", "Bellissima visita", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, volontari);

        TipoVisita nonAssociata = new TipoVisita("Non_Jhonny", "Disneyland", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>());

        session.addVisita(associata);
        session.addVisita(nonAssociata);

        session.salva();
        session.getVisite().clear();
        session.carica();

        Set<TipoVisita> visiteAssociate = ((Volontario) finale).getVisiteAssociate(session.getVisite());

        visiteAssociate.forEach(
                (visita) -> assertTrue(visita.getVolontariIdonei().contains(finale),
                        "Problema nel filtro visite associate")
        );
        assertFalse(visiteAssociate.contains(nonAssociata), "Problema nel filtro visite non associate");

    }

    @Test
    void backupStorico() {
        Session session = new Session();
        session.setVisite(new HashSet<>());
        session.getFilemanager().salva(FileManager.fileStorico, null);

        TipoVisita visita1 = new TipoVisita("Visita1", "Disneyland", "",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>());
        visita1.addVisita(new Visita(Calendar.getInstance(), StatoVisita.PROPOSTA, 6));
        visita1.addVisita(new Visita(Calendar.getInstance(), StatoVisita.EFFETTUATA, 7));
        session.addVisita(visita1);

        TipoVisita visita2 = new TipoVisita("Visita2", "Disneyland", "",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>());
        visita2.addVisita(new Visita(Calendar.getInstance(), StatoVisita.PROPOSTA, 8));
        visita2.addVisita(new Visita(Calendar.getInstance(), StatoVisita.EFFETTUATA, 9));
        session.addVisita(visita2);

        session.salva();
        session.carica();

        assertEquals(session.getStoricoVisite().size(), 2, "Problema nel salvataggio dello storico");

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
    @Order(11)
    void letturaScritturaDatePrecluse() {
        Session session = new Session();

        Set<Integer> testCalendarDatePrecluse = new HashSet<>();
        testCalendarDatePrecluse.add(24);
        testCalendarDatePrecluse.add(21);
        testCalendarDatePrecluse.add(16);
        TipoVisita.aggiungiDatePrecluseFuture(testCalendarDatePrecluse);

        session.salvaParametriGlobali();
        session.caricaParametriGlobali();

        TipoVisita.getDatePrecluseFuture().forEach((calendar) ->
                assertFalse(testCalendarDatePrecluse.add(calendar),
                        "problema lettura/scrittura date precluse i+2"));
    }

    @Test
    @Order(12)
    void salvataggioDatePrecluseCambioMese() {
        Session session = new Session();
        session.caricaParametriGlobali();

        Set<Integer> testCalendarDatePrecluse = new HashSet<>(TipoVisita.getDatePrecluseFuture());

        session.salvataggioDatePrecluseFutureInAttuali();

        TipoVisita.clearDatePrecluseFuture();
        TipoVisita.getDatePrecluseAttuali().clear();

        session.caricaParametriGlobali();

        TipoVisita.getDatePrecluseAttuali().forEach((calendar) -> assertFalse(testCalendarDatePrecluse.add(calendar),
                "problema lettura/scrittura date precluse i+1"));

        assertTrue(TipoVisita.getDatePrecluseFuture().isEmpty(), "problema lettura/scrittura date precluse i+1");
    }

    @Test
    void rimuoviVolontario() {
        Session session = new Session();

        session.setVisite(new HashSet<>());
        session.setLuoghi(new HashSet<>());
        session.setUtenti(new HashSet<>());

        Luogo luogoSopp = new Luogo("LuogoSoppravive", "boh");
        luogoSopp.addVisita("Visita non da distruggere");
        session.addLuogo(luogoSopp);

        Luogo luogoDead = new Luogo("LuogoNonSoppravive", "boh");
        luogoDead.addVisita("Visita da distruggere");
        session.addLuogo(luogoDead);

        Volontario volontario = new Volontario("Volontario", "volontario");
        Set<Utente> volontari = new HashSet<>();
        volontari.add(volontario);
        Set<Volontario> volontariPerVisita = new HashSet<>();
        volontariPerVisita.add(volontario);

        session.setUtenti(volontari);
        session.addVisita(new TipoVisita("Visita da distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisita));

        session.getVisite().clear();
        session.getLuoghi().removeIf((luogo -> luogo.getVisiteIds().contains("Visita da distruggere")));
        session.checkCondizioniDiClassi();

        assertTrue(session.getLuoghi().contains(luogoSopp), "Problema eliminazione luoghi dopo tipovisita");
        assertFalse(session.getLuoghi().contains(luogoDead), "Problema eliminazione luoghi dopo tipovisita");
        assertTrue(session.getVolontari().isEmpty(), "Problema eliminazione volontari");
    }

    @Test
    void rimuoviLuogo() {

    }

    @Test
    void rimuoviTipoVisita() {

    }
}