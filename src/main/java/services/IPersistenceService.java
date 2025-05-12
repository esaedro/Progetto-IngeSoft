package services;

import java.util.HashMap;
import java.util.Set;

public interface IPersistenceService {
    <T> void salvaDati(Set<T> dati);
    <T> Set<T> caricaDati(Class<T> tipo);
    <K, V> HashMap<K, Set<V>> caricaDatiArchivioStorico(Class<K> tipoChiave, Class<V> tipoValore);
    <K, V> void salvaDatiArchivioStorico(HashMap<K, Set<V>> dati);
    void salvaParametriGlobali();
    void caricaParametriGlobali();
}
