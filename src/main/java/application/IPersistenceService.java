package application;

import java.util.HashMap;
import java.util.Set;

public interface IPersistenceService {
    <T> void salvaDati(Set<T> dati);
    <T> Set<T> caricaDati(Class<T> tipo);
    <K, V> HashMap<K, Set<V>> caricaDatiArchioStorico(Class<K> tipoChiave, Class<V> tipoValore);
    <K, V> void salvaDatiArchivioStorico(HashMap<K, Set<V>> dati);
    void salvaParametriGlobali();
    void caricaParametriGlobali();
}
