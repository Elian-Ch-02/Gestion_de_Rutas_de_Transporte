// Stop.java (Modificada para incluir posiciones x, y)
package gestion_de_rutas_de_transporte;

/**
 * Clase que representa una parada en el sistema de rutas de transporte.
 * Contiene un identificador único, un nombre y posiciones para dibujo.
 * @author Elian
 */
public class Stop {
    private int id;
    private String name;
    private int x, y; // Posiciones para dibujo

    public Stop(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Stop{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stop stop = (Stop) obj;
        return id == stop.id;
    }
}
