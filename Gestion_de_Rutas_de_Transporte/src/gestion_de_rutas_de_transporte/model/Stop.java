/**
 * Clase que representa una parada en el sistema de rutas de transporte.
 * Contiene un identificador único, un nombre y coordenadas (x, y) para visualización gráfica.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.model;


public class Stop {
    private int id;
    private String name;
    private int x, y; // Posiciones para dibujo

    /**
     * Constructor de la parada.
     * @param id Identificador único.
     * @param name Nombre de la parada.
     * @param x Coordenada X.
     * @param y Coordenada Y.
     */
    public Stop(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }
/**
     * Obtiene el ID.
     * @return ID de la parada.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID.
     * @param id Nuevo ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre.
     * @return Nombre de la parada.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre.
     * @param name Nuevo nombre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la coordenada X.
     * @return Coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Establece la coordenada X.
     * @param x Nueva coordenada X.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Obtiene la coordenada Y.
     * @return Coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Establece la coordenada Y.
     * @param y Nueva coordenada Y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Retorna una representación en string de la parada.
     * @return String con detalles de la parada.
     */
    @Override
    public String toString() {
        return "Stop{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    /**
     * Compara si dos paradas son iguales por ID.
     * @param obj Objeto a comparar.
     * @return true si iguales, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stop stop = (Stop) obj;
        return id == stop.id;
    }
}