package application;

import java.util.Set;

public class PlaceServiceImpl implements IPlaceService {

    private Set<Luogo> luoghi;
    private IPersistenceService persistenceService;

    public PlaceServiceImpl(ServiceFactory factory) {
        persistenceService = factory.createPersistenceService();
    }

    @Override
    public Set<Luogo> getLuoghi() {
        return luoghi;
    }

    @Override
    public void caricaLuoghi() {
        luoghi = persistenceService.caricaDati(Luogo.class);
    }

    @Override
    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
    }

    @Override
    public void setLuoghi(Set<Luogo> luoghiDaAggiungere) {
        luoghi = luoghiDaAggiungere;
    }

    @Override
    public void rimuoviLuoghi(Set<Luogo> luoghiDaRimuovere) {
        luoghi.removeAll(luoghiDaRimuovere);
    }

    @Override
    public void controllaCondizioniLuogo() {
        luoghi.removeIf(luogo -> !luogo.haVisiteAssociate());
    }

    @Override
    public void aggiungiLuoghi(Set<Luogo> luoghiDaAggiungere) {
        luoghi.addAll(luoghiDaAggiungere);
    }

    @Override
    public void salvaLuoghi() {
        persistenceService.salvaDati(luoghi);
    }
}
