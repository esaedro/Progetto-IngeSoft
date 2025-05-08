package application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import utility.FileManager;

public class PersistenceServiceImpl implements IPersistenceService {

    private final FileManager fileManager;

    public PersistenceServiceImpl() {
        this.fileManager = FileManager.getInstance();
    }

    private <T> String determinaNomeFile(Class<T> tipo) {
        if (
            Utente.class.isAssignableFrom(tipo) ||
            Configuratore.class.isAssignableFrom(tipo) ||
            Volontario.class.isAssignableFrom(tipo) ||
            Fruitore.class.isAssignableFrom(tipo)
        ) {
            return FileManager.fileUtenti;
        } else if (Luogo.class.isAssignableFrom(tipo)) {
            return FileManager.fileLuoghi;
        } else if (TipoVisita.class.isAssignableFrom(tipo)) {
            return FileManager.fileVisite;
        } else {
            throw new IllegalArgumentException("Tipo non supportato: " + tipo.getName());
        }
    }

    @Override
    public <T> void salvaDati(Set<T> dati) {
        // if (dati.isEmpty()) throw new IllegalStateException("Nessun dato da salvare");
        if (dati == null || dati.isEmpty()) return;
        String nomeFile = determinaNomeFile(dati.iterator().next().getClass());
        fileManager.salva(nomeFile, dati);
    }

    @Override
    public <T> Set<T> caricaDati(Class<T> tipo) {
        String nomeFile = determinaNomeFile(tipo);
        Set<T> datiCaricati = fileManager.carica(nomeFile, tipo);
        return datiCaricati != null ? datiCaricati : new HashSet<>();
    }

    @Override
    public <K, V> HashMap<K, Set<V>> caricaDatiArchioStorico(
        Class<K> tipoChiave,
        Class<V> tipoValore
    ) {
        return fileManager.caricaStorico(FileManager.fileStorico, tipoChiave, tipoValore);
    }

    @Override
    public <K, V> void salvaDatiArchivioStorico(HashMap<K, Set<V>> dati) {
        fileManager.salva(FileManager.fileStorico, dati);
    }

    @Override
    public void salvaParametriGlobali() {
        fileManager.salvaParametriGlobali();
    }

    @Override
    public void caricaParametriGlobali() {
        fileManager.caricaParametriGlobali();
    }
}
