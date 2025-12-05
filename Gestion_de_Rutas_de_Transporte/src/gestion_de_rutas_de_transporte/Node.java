/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion_de_rutas_de_transporte;

/**
 * Clase genérica que representa un nodo en una lista enlazada o cola.
 * @param <T> Tipo de dato almacenado en el nodo.
 * @author Elian
 */
public class Node<T> {
    private T data;
    private Node<T> next;

    /**
     * Constructor del nodo.
     * @param data Dato a almacenar.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Obtiene el dato almacenado.
     * @return Dato del nodo.
     */
    public T getData() {
        return data;
    }

    /**
     * Establece el dato del nodo.
     * @param data Nuevo dato.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene el siguiente nodo.
     * @return Siguiente nodo.
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Establece el siguiente nodo.
     * @param next Nuevo siguiente nodo.
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }
}