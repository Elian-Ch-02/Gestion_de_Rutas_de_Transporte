/**
 * Clase auxiliar para almacenar un par de valores (e.g., vecino y peso en aristas).
 * Compone dos campos genéricos para el primer y segundo elemento.
 * @param <T> Tipo del primer elemento.
 * @param <U> Tipo del segundo elemento.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.model;


public class Pair<T, U> {
    public T first;
    public U second;
    
/**
* Constructor del par.
* @param first Primer elemento.
* @param second Segundo elemento.
*/    
    Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Obtiene el primer elemento.
     * @return Primer elemento.
     */
    public T getFirst() { return first; }
    
    /**
     * Obtiene el segundo elemento.
     * @return Segundo elemento.
     */
    public U getSecond() { return second; }
}
