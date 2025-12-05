/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion_de_rutas_de_transporte;

/**
 * Clase genérica que implementa una lista enlazada simple.
 * Se utiliza para almacenar paradas, rutas, horarios y listas de adyacencia en el grafo.
 * Justificación: Las listas enlazadas son eficientes para inserciones y eliminaciones en secuencias como paradas en rutas.
 * @param <T> Tipo de dato en la lista.
 * @author Elian
 */
public class CustomLinkedList<T> {
    Node<T> head;
    private int size;

    /**
     * Constructor de la lista enlazada.
     */
    public CustomLinkedList() {
        head = null;
        size = 0;
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
     * Reconstruye la lista desde un arreglo.
     * @param array Arreglo de elementos.
     */
    public void fromArray(T[] array) {
        clear();
        for (T item : array) {
            add(item);
        }
    }
}