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
        Utente utenteProvvisorio = new Utente(new Session());
        ArrayList<Utente> utentiTest = new ArrayList<>();
        utentiTest.add(new Configuratore("C_Dilbert", "admin"));
        utenteProvvisorio.getSession().setUtenti(utentiTest);
        utenteProvvisorio.getSession().salvaUtenti();

        Utente finale = utenteProvvisorio.login("C_Dilbert", "admin");

        assertInstanceOf(Configuratore.class, finale,"Login configuratore non restituisce i permessi");
    }

    @Test
    @Order(2)
    void disponibilitaVolontari() {
        Utente utenteProvvisorio = new Utente(new Session());
        Set<Integer> disp = new HashSet<>();
        disp.add(3);
        disp.add(4);
        disp.add(8);

        ArrayList<Utente> utentiTest = new ArrayList<>();
        utentiTest.add(new Volontario("V_Jhonny", "volontario", disp));

        utenteProvvisorio.getSession().setUtenti(utentiTest);
        utenteProvvisorio.getSession().salvaUtenti();

        Utente finale = utenteProvvisorio.login("V_Jhonny", "volontario");
        assertInstanceOf(Volontario.class, finale,"Login volontario non restituisce i permessi");

        ((Volontario)finale).getDisponibilita().forEach(giorno ->
                assertFalse(disp.add(giorno), "errore lettura/scrittura disponibilità volontario"));

    }

    @Test
    @Order(3)
    void getDisponibilitaVolontario() {
        Utente utenteProvvisorio = new Utente(new Session());
        ArrayList<Utente> utentiTest = new ArrayList<>();
        utentiTest.add(new Volontario("V_Jhonny", "volontario"));
        utenteProvvisorio.getSession().setUtenti(utentiTest);
        utenteProvvisorio.getSession().salvaUtenti();

        Utente finale = utenteProvvisorio.login("V_Jhonny", "volontario");

        finale.getSession().setVisite(new ArrayList<>());

        Set<Volontario> volontari = new HashSet<>();
        volontari.add((Volontario) finale);

        Visita associata = new Visita("Jhonny_visita", "Bellissima visita", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, volontari, Calendar.getInstance(),
                StatoVisita.PROPOSTA, 6);

        Visita nonAssociata = new Visita("Non_Jhonny", "Bellissima visita", "Disneyland",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>(), Calendar.getInstance(),
                StatoVisita.PROPOSTA, 6);

        utenteProvvisorio.getSession().addVisita(associata);
        utenteProvvisorio.getSession().addVisita(nonAssociata);

        finale.getSession().salva();
        finale.getSession().getVisite().clear();
        finale.getSession().carica();

        System.out.println(((Volontario) finale).getDisponibilita());
        ArrayList<Visita> visiteAssociate = ((Volontario) finale).getVisiteAssociate();

        assertTrue(visiteAssociate.get(0).getVolontariIdonei().contains(finale), "Problema nel filtro visite associate");
        assertFalse(visiteAssociate.contains(nonAssociata), "Problema nel filtro visite non associate");

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

        TipoVisita.getDatePrecluseFuture().forEach((calendar) -> {
            assertFalse(testCalendarDatePrecluse.add(calendar), "problema lettura/scrittura date precluse i+2");
        });
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



}