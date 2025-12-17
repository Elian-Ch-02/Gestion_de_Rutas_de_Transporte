/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion_de_rutas_de_transporte;

/**
 * Clase que implementa el algoritmo de ordenamiento Burbuja.
 * Se utiliza para ordenar las paradas por nombre alfabéticamente.
 * @author Elian
 */
public class Sorter {
    /**
     * Ordena una lista enlazada de paradas utilizando el algoritmo de burbuja.
     * @param list Lista enlazada de paradas a ordenar.
     */
    public static void bubbleSort(CustomLinkedList<Stop> list) {
        Stop[] array = list.toArray();
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j].getName().compareTo(array[j + 1].getName()) > 0) {
                    // Intercambio de elementos
                    Stop temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        list.fromArray(array);
    }
}