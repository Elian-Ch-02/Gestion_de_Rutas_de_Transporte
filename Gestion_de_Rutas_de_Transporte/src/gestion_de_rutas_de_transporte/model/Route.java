/**
 * Clase que representa una ruta en el sistema.
 * Incluye identificador, nombre, lista de IDs de paradas y un color para visualización.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.model;

import gestion_de_rutas_de_transporte.utils.CustomLinkedList;
import java.awt.Color;


public class Route {
    private int id;
    private String name;
    private CustomLinkedList<Integer> stopIds;
    private Color color;  // Color de la ruta
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
     * Obtiene el color de la ruta.
     * @return Color de la ruta.
     */
    public Color getColor() {
    return color;
}

    /**
     * Establece el nombre de la ruta.
     * @param name Nuevo nombre.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Establece el color de la ruta.
     * @param color Nuevo color.
     */
    public void setColor(Color color) {
    this.color = color;
}
    

    /**
     * Obtiene la lista de ids de paradas en la ruta.
     * @return Lista enlazada de ids de paradas.
     */
    public CustomLinkedList<Integer> getStopIds() {
        return stopIds;
    }

    /**
     * Retorna una representación en string de la ruta.
     * @return String con detalles de la ruta.
     */
    @Override
    public String toString() {
        return "Route{" + "id=" + id + ", name='" + name + "', stops=" + stopIds + '}';
    }

    /**
     * Compara si dos rutas son iguales por ID.
     * @param obj Objeto a comparar.
     * @return true si iguales, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Route route = (Route) obj;
        return id == route.id;
    }
}
