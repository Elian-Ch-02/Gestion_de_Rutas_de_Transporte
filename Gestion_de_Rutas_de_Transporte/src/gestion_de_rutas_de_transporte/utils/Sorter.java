/**
 * Clase que implementa el algoritmo de ordenamiento Burbuja.
 * Se utiliza para ordenar listas enlazadas de paradas por nombre alfabéticamente.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.utils;

import gestion_de_rutas_de_transporte.utils.CustomLinkedList;
import gestion_de_rutas_de_transporte.model.Stop;

public class Sorter {
    /**
     * Ordena una lista enlazada de paradas utilizando el algoritmo de burbuja.
     * Convierte temporalmente a arreglo para ordenar y reconstruye la lista.
     * @param list Lista enlazada de paradas a ordenar.
     */
   public static void bubbleSort(CustomLinkedList<Stop> list, String criterio) {
    Stop[] array = list.toArray(Stop.class);  // Usa el nuevo método con Stop.class
    int n = array.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            boolean swap = false;
            if ("nombre".equalsIgnoreCase(criterio)) {
                if (array[j].getName().compareTo(array[j + 1].getName()) > 0) {
                    swap = true;
                }
            } else if ("id".equalsIgnoreCase(criterio)) {
                if (array[j].getId() > array[j + 1].getId()) {
                    swap = true;
                }
            }
            if (swap) {
                // Intercambio de elementos
                Stop temp = array[j];
                array[j] = array[j + 1];
                array[j + 1] = temp;
            }
        }
    }
    list.fromArray(array);
}
   public static void bubbleSort(CustomLinkedList<Stop> list) {
    bubbleSort(list, "nombre");
   }
}