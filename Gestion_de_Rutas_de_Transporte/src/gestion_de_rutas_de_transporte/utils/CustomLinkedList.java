/**
 * Clase genérica que implementa una lista enlazada simple.
 * Se utiliza para almacenar paradas, rutas, horarios y listas de adyacencia en el grafo.
 * Soporta operaciones como agregar, remover, buscar, iterar y convertir a arreglo.
 * Compone un nodo cabeza y un contador de tamaño.
 * @param <T> Tipo de dato en la lista.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.utils;

import gestion_de_rutas_de_transporte.model.Node;
import gestion_de_rutas_de_transporte.model.Pair;
import gestion_de_rutas_de_transporte.model.Schedule;
import gestion_de_rutas_de_transporte.model.Route;
import gestion_de_rutas_de_transporte.model.Stop;
import java.util.function.Consumer;

public class CustomLinkedList<T> {
    public Node<T> head;
    private int size;

    /**
     * Constructor de la lista enlazada.
     */
    public CustomLinkedList() {
        head = null;
        size = 0;
    }
    
    /**
     * Itera sobre la lista aplicando una acción a cada elemento.
     * @param action Acción a aplicar.
     */
    public void iterate(Consumer<T> action) {
    Node<T> current = head;
    while (current != null) {
        action.accept(current.getData());
        current = current.getNext();
    }
}

    /**
     * Agrega un elemento al final de la lista.
     * @param data Dato a agregar.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Remueve un elemento por valor.
     * @param data Dato a remover.
     * @return true si removido, false otherwise.
     */
    public boolean remove(T data) {
        if (head == null) return false;
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }
        Node<T> current = head;
        while (current.getNext() != null) {
            if (current.getNext().getData().equals(data)) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Busca un elemento por clave.
     * @param key Clave de búsqueda (ej. id).
     * @return Elemento encontrado o null.
     */
    public T search(Object key) {
        Node<T> current = head;
        while (current != null) {
            if (matches(current.getData(), key)) {
                return current.getData();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Verifica si un dato coincide con la clave (para tipos específicos como Stop, Route, etc.).
     * @param data Dato a verificar.
     * @param key Clave.
     * @return true si coincide.
     */
    private boolean matches(T data, Object key) {
        if (data instanceof Stop && key instanceof Integer) {
            return ((Stop) data).getId() == (Integer) key;
        } else if (data instanceof Route && key instanceof Integer) {
            return ((Route) data).getId() == (Integer) key;
        } else if (data instanceof Schedule && key instanceof Integer) {
            return ((Schedule) data).getId() == (Integer) key;
        } else if (data instanceof Integer && key instanceof Integer) {
            return data.equals(key);
        }
        return data.equals(key);
    }

    /**
     * Muestra todos los elementos en consola.
     */
    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }
    }

    /**
     * Obtiene el tamaño de la lista.
     * @return Tamaño.
     */
    public int getSize() {
        return size;
    }

    /**
     * Obtiene el elemento en un índice específico.
     * @param index Índice.
     * @return Elemento o null si inválido.
     */
    public T getAt(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    /**
     * Limpia la lista.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Convierte la lista a un arreglo.
     * @return Arreglo de elementos.
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] array = (T[]) new Object[size];
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            array[i++] = current.getData();
            current = current.getNext();
        }
        return array;
    }
    
    /**
     * Busca un elemento por clave entera (para compatibilidad).
     * @param key Clave entera.
     * @return Elemento encontrado o null.
     */
    public T search(int key) {
    Node<T> current = head;
    while (current != null) {
        if (current.getData() instanceof Stop && ((Stop) current.getData()).getId() == key) return current.getData();
        // Similar para Route y Schedule
        current = current.getNext();
    }
    return null;
}

    /**
     * Reconstruye la lista desde un arreglo.
     * @param array Arreglo de elementos.
     */
    public void fromArray(T[] array) {
        clear();
        for (T item : array) {
            add(item);
        }
    }

    /**
     * Obtiene el nodo cabeza de la lista.
     * @return Nodo cabeza.
     */
    public Node<T> getHead() {
    return head;  // Simplemente retorna el head
    }
}