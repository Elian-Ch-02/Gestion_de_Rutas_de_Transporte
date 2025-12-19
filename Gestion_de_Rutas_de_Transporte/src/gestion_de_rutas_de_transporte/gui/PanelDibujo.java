/**
 * Panel personalizado para dibujar el grafo de paradas y rutas.
 * Extiende JPanel y maneja la renderización de vértices (paradas) y aristas (rutas) con colores.
 * Compone referencias al grafo, listas de paradas y rutas, y listas para rutas seleccionadas.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.gui;


import gestion_de_rutas_de_transporte.utils.CustomLinkedList;
import gestion_de_rutas_de_transporte.model.Node;
import gestion_de_rutas_de_transporte.model.Graph;
import gestion_de_rutas_de_transporte.model.Route;
import gestion_de_rutas_de_transporte.model.Stop;

import java.awt.*;
import javax.swing.*;

/**
 * Panel personalizado para dibujar el grafo.
 */
public class PanelDibujo extends JPanel {
    private Graph graph;
    private CustomLinkedList<Route> routes;
    private CustomLinkedList<Stop> stops;
    private CustomLinkedList<Integer> shortestPath;
    private CustomLinkedList<Integer> longestPath;

/**
* Constructor del panel de dibujo. Inicializa referencias y configura propiedades básicas.
* @param graph Grafo a dibujar.
* @param stops Lista de paradas.
* @param routes Lista de rutas.
*/    
   public PanelDibujo(Graph graph, CustomLinkedList<Stop> stops, CustomLinkedList<Route> routes) {
    this.graph = graph;
    this.stops = stops;
    this.routes = routes;
    this.shortestPath = new CustomLinkedList<>();
    this.longestPath = new CustomLinkedList<>();
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(600, 400));
}

/**
* Establece la ruta más corta para resaltar en el dibujo.
* @param path Lista de IDs de paradas en la ruta.
*/
    public void setShortestPath(CustomLinkedList<Integer> path) {
        this.shortestPath = path;
        repaint();
    }

/**
* Establece la ruta más larga para resaltar en el dibujo.
* @param path Lista de IDs de paradas en la ruta.
*/
    public void setLongestPath(CustomLinkedList<Integer> path) {
        this.longestPath = path;
        repaint();
    }
    
    private CustomLinkedList<Integer> selectedRoutePath = new CustomLinkedList<>();

    
    /**
     * Establece la ruta seleccionada para resaltar en el dibujo.
     * @param path Lista de IDs de paradas en la ruta.
     */
    public void setSelectedRoutePath(CustomLinkedList<Integer> path) {
        this.selectedRoutePath = path;
    }

    /**
     * Método sobrescrito para dibujar el componente. Renderiza aristas, rutas seleccionadas y vértices.
     * @param g Objeto Graphics para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar aristas (rutas con colores)
        g2d.setStroke(new BasicStroke(3f));  // Grosor para resaltar rutas

        Node<Route> routeNode = routes.getHead();
        while (routeNode != null) {
            Route route = routeNode.getData();
            g2d.setColor(route.getColor() != null ? route.getColor() : Color.BLACK);  // Negro si null
            CustomLinkedList<Integer> stopIds = route.getStopIds();
            for (int j = 0; j < stopIds.getSize() - 1; j++) {
                Stop origin = getStopById(stopIds.getAt(j));
                Stop dest = getStopById(stopIds.getAt(j + 1));
                if (origin != null && dest != null) {
                    g2d.drawLine(origin.getX(), origin.getY(), dest.getX(), dest.getY());
                }
            }
            routeNode = routeNode.getNext();
        }

        g2d.setColor(Color.BLACK);  // Reset para otros dibujos
        g2d.setStroke(new BasicStroke(1f));

        // Dibujar ruta seleccionada (si aplica)
        g2d.setStroke(new BasicStroke(3f));
        for (int i = 0; i < selectedRoutePath.getSize() - 1; i++) {
            Stop origin = getStopById(selectedRoutePath.getAt(i));
            Stop dest = getStopById(selectedRoutePath.getAt(i + 1));
            if (origin != null && dest != null) {
                g2d.drawLine(origin.getX(), origin.getY(), dest.getX(), dest.getY());
            }
        }

        // Dibujar vértices (paradas)
        g2d.setFont(new Font("Arial", Font.BOLD, 12));  // Fuente más legible
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 0; i < stops.getSize(); i++) {
            Stop stop = stops.getAt(i);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(stop.getX() - 20, stop.getY() - 20, 40, 40);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(stop.getX() - 20, stop.getY() - 20, 40, 40);
            // Mejor posicionamiento: centrado debajo del círculo para evitar solapamientos
            String label = stop.getName() + " (" + stop.getId() + ")";
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            g2d.drawString(label, stop.getX() - labelWidth / 2, stop.getY() + 30);  // Debajo del círculo
        }    
    }

    /**
     * Obtiene una parada por su ID.
     * @param id ID de la parada.
     * @return La parada encontrada o null si no existe.
     */
    private Stop getStopById(int id) {
        Node<Stop> current = stops.head;
        while (current != null) {
            Stop stop = current.getData();
            if (stop.getId() == id) {
                return stop;
            }
            current = current.getNext();
        }
        return null;
    }

}