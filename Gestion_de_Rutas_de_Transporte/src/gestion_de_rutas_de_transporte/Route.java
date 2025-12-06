/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion_de_rutas_de_transporte;

/**
 * Clase que representa una ruta en el sistema.
 * Incluye identificador, nombre y una lista de ids de paradas.
 * @author Elian
 */
public class Route {
    private int id;
    private String name;
    private CustomLinkedList<Integer> stopIds;

    /**
     * Constructor de la clase Route.
     * @param id Identificador único de la ruta.
     * @param name Nombre de la ruta.
     */
    public Route(int id, String name) {
        this.id = id;
        this.name = name;
        this.stopIds = new CustomLinkedList<>();
    }

    /**
     * Obtiene el identificador de la ruta.
     * @return id de la ruta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador de la ruta.
     * @param id Nuevo identificador.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la ruta.
     * @return Nombre de la ruta.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la ruta.
     * @param name Nuevo nombre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la lista de ids de paradas en la ruta.
     * @return Lista enlazada de ids de paradas.
     */
    public CustomLinkedList<Integer> getStopIds() {
        return stopIds;
    }

    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name='" + name + "', stops=" + stopIds + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Route route = (Route) obj;
        return id == route.id;
    }
}
