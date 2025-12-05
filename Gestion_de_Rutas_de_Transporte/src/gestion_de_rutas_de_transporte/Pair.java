// Pair.java (Nueva clase agregada)
package gestion_de_rutas_de_transporte;

/**
 * Clase auxiliar para retornar un par de valores (e.g., vecino y peso).
 * @param <T> Tipo del primer elemento.
 * @param <U> Tipo del segundo elemento.
 */
class Pair<T, U> {
    T first;
    U second;

    Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
}
