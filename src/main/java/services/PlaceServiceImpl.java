package services;

import java.util.Set;

import application.Luogo;

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

    /**
     * @ requires luogo != null;
     */
    @Override
    public void aggiungiLuogo(Luogo luogo) {
        luoghi.add(luogo);
    }

    /**
     * @ requires luoghi != null;
     */
    @Override
    public void setLuoghi(Set<Luogo> luoghi) {
        this.luoghi = luoghi;
    }

    /**
     * @ requires luoghiDaRimuovere != null;
     */
    @Override
    public void rimuoviLuoghi(Set<Luogo> luoghiDaRimuovere) {
        luoghi.removeAll(luoghiDaRimuovere);
    }

    @Override
    public void controllaCondizioniLuogo() {
        luoghi.removeIf(luogo -> !luogo.haVisiteAssociate());
    }

    /**
     * @ requires luoghiDaAggiungere != null;
     */
    @Override
    public void aggiungiLuoghi(Set<Luogo> luoghiDaAggiungere) {
        luoghi.addAll(luoghiDaAggiungere);
    }

    @Override
    public void salvaLuoghi() {
        persistenceService.salvaDati(luoghi);
    }
}
