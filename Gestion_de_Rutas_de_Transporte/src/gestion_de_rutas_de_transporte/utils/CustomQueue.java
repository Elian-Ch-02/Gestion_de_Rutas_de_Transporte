/**
 * Clase genérica que implementa una cola utilizando nodos enlazados.
 * Se utiliza en algoritmos como BFS en el grafo.
 * Compone referencias al frente y final de la cola.
 * @param <T> Tipo de dato en la cola.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.utils;

import gestion_de_rutas_de_transporte.model.Node;

public class CustomQueue<T> {
    private Node<T> front;
    private Node<T> rear;

    /**
     * Constructor de la cola.
     */
    public CustomQueue() {
        front = null;
        rear = null;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param data Dato a agregar.
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
    }

    /**
     * Remueve y retorna el elemento al frente de la cola.
     * @return Dato removido o null si vacía.
     */
    public T dequeue() {
        if (front == null) return null;
        T data = front.getData();
        front = front.getNext();
        if (front == null) rear = null;
        return data;
    }

    /**
     * Verifica si la cola está vacía.
     * @return true si vacía, false otherwise.
     */
    public boolean isEmpty() {
        return front == null;
    }
}
