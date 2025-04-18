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

        finale = session.login("Errore", "ErroreMaPassword");
        assertNull(finale, "Erroro login configuratore");
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

        finale = session.login("Errore", "ErroreMaPassword");
        assertNull(finale, "Erroro volontario fruitore");

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

        Visita visita1Proposta = new Visita(visita1.getTitolo(), Calendar.getInstance(), StatoVisita.PROPOSTA, 6);
        Visita visita1Effettuata = new Visita(visita1.getTitolo(), Calendar.getInstance(), StatoVisita.EFFETTUATA, 7);

        visita1.addVisita(visita1Proposta);
        visita1.addVisita(visita1Effettuata);
        session.addVisita(visita1);

        TipoVisita visita2 = new TipoVisita("Visita2", "Disneyland", "",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 2, new HashSet<>(),
                5, 10, true, new HashSet<>());

        Visita visita2Proposta = new Visita(visita2.getTitolo(),Calendar.getInstance(), StatoVisita.PROPOSTA, 8);
        Visita visita2Effettuata = new Visita(visita2.getTitolo(), Calendar.getInstance(), StatoVisita.EFFETTUATA, 9);

        visita2.addVisita(visita2Proposta);
        visita2.addVisita(visita2Effettuata);
        session.addVisita(visita2);

        session.salva();
        session.carica();

        HashMap<String, Set<Visita>> visiteStorico = session.getStoricoVisite();

        visiteStorico.get("Visita1").forEach((v) -> assertEquals(v.getStato(), StatoVisita.EFFETTUATA, "Problema nel salvataggio dello storico"));
        visiteStorico.get("Visita2").forEach((v) -> assertEquals(v.getStato(), StatoVisita.EFFETTUATA, "Problema nel salvataggio dello storico"));

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
    void rimuoviTipoVisita() {
        Session session = new Session();

        session.setVisite(new HashSet<>());
        session.setLuoghi(new HashSet<>());
        session.setUtenti(new HashSet<>());

        Luogo luogoDaNonDistruggere = new Luogo("LuogoSoppravive", "boh");
        luogoDaNonDistruggere.addVisita("Visita non da distruggere");
        session.addLuogo(luogoDaNonDistruggere);

        Luogo luogoDaDistruggere = new Luogo("LuogoNonSoppravive", "boh");
        luogoDaDistruggere.addVisita("Visita da distruggere");
        session.addLuogo(luogoDaDistruggere);

        Volontario volontarioDaDistruggere = new Volontario("Volontario", "volontario");
        Set<Utente> volontari = new HashSet<>();
        volontari.add(volontarioDaDistruggere);
        Set<Volontario> volontariPerVisita = new HashSet<>();
        volontariPerVisita.add(volontarioDaDistruggere);

        session.setUtenti(volontari);
        TipoVisita tipoVisitaDaDistruggere = new TipoVisita("Visita da distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisita);
        session.addVisita(tipoVisitaDaDistruggere);

        Set<TipoVisita> tipoVisiteDaDistruggere = new HashSet<>();
        tipoVisiteDaDistruggere.add(tipoVisitaDaDistruggere);

        session.removeTipoVisita(tipoVisiteDaDistruggere);
        session.checkCondizioniDiClassi();

        assertTrue(session.getLuoghi().contains(luogoDaNonDistruggere), "Problema eliminazione luoghi dopo tipovisita");
        assertFalse(session.getLuoghi().contains(luogoDaDistruggere), "Problema eliminazione luoghi dopo tipovisita");
        assertTrue(session.getVolontari().isEmpty(), "Problema eliminazione volontari");
    }

    @Test
    void rimuoviLuogo() {
        Session session = new Session();

        session.setVisite(new HashSet<>());
        session.setLuoghi(new HashSet<>());
        session.setUtenti(new HashSet<>());

        Luogo luogoDaDistruggere = new Luogo("LuogoDaDistruggere", "boh");
        luogoDaDistruggere.addVisita("Visita da distruggere");
        session.addLuogo(luogoDaDistruggere);

        Volontario volontarioDaDistruggere = new Volontario("Volontario", "volontario");
        Set<Utente> volontari = new HashSet<>();
        volontari.add(volontarioDaDistruggere);

        Set<Volontario> volontariPerVisita = new HashSet<>();
        volontariPerVisita.add(volontarioDaDistruggere);

        TipoVisita visitaDaDistruggere = new TipoVisita("Visita da distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisita);
        session.addVisita(visitaDaDistruggere);

        Luogo luogoDaNonDistruggere = new Luogo("Luogo", "boh");
        luogoDaNonDistruggere.addVisita("Visita da non distruggere");
        session.addLuogo(luogoDaNonDistruggere);

        Volontario volontarioNonDaDistruggere = new Volontario("VolontarioForte", "volontario");
        volontari.add(volontarioNonDaDistruggere);

        Set<Volontario> volontariPerVisitaNonDaDistruggere = new HashSet<>();
        volontariPerVisitaNonDaDistruggere.add(volontarioNonDaDistruggere);

        session.setUtenti(volontari);

        TipoVisita visitaNonDaDistruggere = new TipoVisita("Visita da non distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisitaNonDaDistruggere);
        session.addVisita(visitaNonDaDistruggere);

        Set<Luogo> luoghiDaRimuovere = new HashSet<>();
        luoghiDaRimuovere.add(luogoDaDistruggere);
        session.removeLuoghi(luoghiDaRimuovere);

        session.checkCondizioniDiClassi();

        assertTrue(session.getVolontari().contains(volontarioNonDaDistruggere), "Problema eliminazione volontari dopo luogo");
        assertFalse(session.getVolontari().contains(volontarioDaDistruggere), "Problema eliminazione volontari dopo luogo");
        assertTrue(session.getVisite().contains(visitaNonDaDistruggere), "Problema eliminazione visita dopo luogo");
        assertFalse(session.getVisite().contains(visitaDaDistruggere), "Problema eliminazione visita dopo luogo");
    }

    @Test
    void rimuoviVolontario() {
        Session session = new Session();

        session.setVisite(new HashSet<>());
        session.setLuoghi(new HashSet<>());
        session.setUtenti(new HashSet<>());

        Luogo luogoDaDistruggere = new Luogo("LuogoDaDistruggere", "boh");
        luogoDaDistruggere.addVisita("Visita da distruggere");
        session.addLuogo(luogoDaDistruggere);

        Volontario volontarioDaDistruggere = new Volontario("Volontario", "volontario");
        Set<Utente> volontari = new HashSet<>();
        volontari.add(volontarioDaDistruggere);

        Set<Volontario> volontariPerVisita = new HashSet<>();
        volontariPerVisita.add(volontarioDaDistruggere);

        TipoVisita visitaDaDistruggere = new TipoVisita("Visita da distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisita);
        session.addVisita(visitaDaDistruggere);

        Luogo luogoDaNonDistruggere = new Luogo("Luogo", "boh");
        luogoDaNonDistruggere.addVisita("Visita da non distruggere");
        session.addLuogo(luogoDaNonDistruggere);

        Volontario volontarioNonDaDistruggere = new Volontario("VolontarioForte", "volontario");
        volontari.add(volontarioNonDaDistruggere);

        Set<Volontario> volontariPerVisitaNonDaDistruggere = new HashSet<>();
        volontariPerVisitaNonDaDistruggere.add(volontarioNonDaDistruggere);

        session.setUtenti(volontari);

        TipoVisita visitaNonDaDistruggere = new TipoVisita("Visita da non distruggere", "descr", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 23, new HashSet<>(),
                23, 24, true, volontariPerVisitaNonDaDistruggere);
        session.addVisita(visitaNonDaDistruggere);

        Set<Volontario> volontariDaDistruggere = new HashSet<>();
        volontariDaDistruggere.add(volontarioDaDistruggere);
        session.removeVolontario(volontariDaDistruggere);

        session.checkCondizioniDiClassi();

        assertTrue(session.getLuoghi().contains(luogoDaNonDistruggere), "Problema eliminazione luogo dopo volontario");
        assertFalse(session.getLuoghi().contains(luogoDaDistruggere), "Problema eliminazione luogo dopo volontario");
        assertTrue(session.getVisite().contains(visitaNonDaDistruggere), "Problema eliminazione visita dopo volontario");
        assertFalse(session.getVisite().contains(visitaDaDistruggere), "Problema eliminazione visita dopo volontario");
    }

    @Test
    void loginFruitoreTest() {
        Session session = new Session();

        Set<Utente> utentiTest = new HashSet<>();
        HashMap<Visita, Iscrizione> iscrizioni = new HashMap<>();
        iscrizioni.put(new Visita("Titolotest1", Calendar.getInstance(), StatoVisita.PROPOSTA, 4),
                new Iscrizione("codice", 2));
        utentiTest.add(new Fruitore("Fruitore", "frutto", iscrizioni));
        session.setUtenti(utentiTest);
        session.salvaUtenti();

        session.setUtenti(new HashSet<>());

        Utente finale = session.login("Fruitore", "frutto");
        assertInstanceOf(Fruitore.class, finale,"Login fruitore non restituisce i permessi");

        finale = session.login("Errore", "ErroreMaPassword");
        assertNull(finale, "Erroro login fruitore");
    }

    @Test
    void scritturaVisitaDentroTipoVisita() {
        Session session = new Session();
        session.setVisite(new HashSet<>());

        Visita visita = new Visita("Titolotest2", Calendar.getInstance(), StatoVisita.PROPOSTA, 5);
        visita.setVolontarioAssociato(new Volontario("volontario", "volontario"));

        TipoVisita tipoVisita = new TipoVisita("Titolo", "Descrizione", "punto",
                Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 3, new HashSet<>(), 1, 5,
                true, new HashSet<>());
        tipoVisita.addVisita(visita);

        session.addVisita(tipoVisita);

        session.salva();
        session.setVisite(new HashSet<>());

        session.carica();

        session.getVisite().forEach((tV) -> tV.getVisiteAssociate().
                forEach(((v) -> assertNotNull(v.getVolontarioAssociato()))));
    }
}