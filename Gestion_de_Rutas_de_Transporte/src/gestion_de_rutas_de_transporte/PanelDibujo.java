// PanelDibujo.java (Nueva clase para dibujo del grafo)
package gestion_de_rutas_de_transporte;

import java.awt.*;
import javax.swing.*;

/**
 * Panel personalizado para dibujar el grafo.
 */
public class PanelDibujo extends JPanel {
    private Graph graph;
    private CustomLinkedList<Stop> stops;
    private CustomLinkedList<Integer> shortestPath;
    private CustomLinkedList<Integer> longestPath;

    public PanelDibujo(Graph graph, CustomLinkedList<Stop> stops) {
        this.graph = graph;
        this.stops = stops;
        this.shortestPath = new CustomLinkedList<>();
        this.longestPath = new CustomLinkedList<>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
    }

    public void setShortestPath(CustomLinkedList<Integer> path) {
        this.shortestPath = path;
        repaint();
    }

    public void setLongestPath(CustomLinkedList<Integer> path) {
        this.longestPath = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar aristas
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < Graph.MAX_STOPS; i++) {
            Node<Pair<Integer, Integer>> neighbor = graph.adjacency[i].head;
            while (neighbor != null) {
                Pair<Integer, Integer> pair = neighbor.getData();
                Stop origin = getStopById(i + 1);
                Stop dest = getStopById(pair.first);
                if (origin != null && dest != null && i + 1 < pair.first) {
                    g2d.drawLine(origin.getX(), origin.getY(), dest.getX(), dest.getY());
                    // Dibujar peso
                    int midX = (origin.getX() + dest.getX()) / 2;
                    int midY = (origin.getY() + dest.getY()) / 2;
                    g2d.setColor(Color.BLUE);
                    g2d.drawString(String.valueOf(pair.second), midX + 5, midY - 5);
                    g2d.setColor(Color.BLACK);
                }
                neighbor = neighbor.getNext();
            }
        }

        // Resaltar ruta corta
        if (shortestPath.getSize() > 1) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(3f));
            for (int i = 0; i < shortestPath.getSize() - 1; i++) {
                Stop origin = getStopById(shortestPath.getAt(i));
                Stop dest = getStopById(shortestPath.getAt(i + 1));
                if (origin != null && dest != null) {
                    g2d.drawLine(origin.getX(), origin.getY(), dest.getX(), dest.getY());
                }
            }
        }

        // Resaltar ruta larga
        if (longestPath.getSize() > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3f));
            for (int i = 0; i < longestPath.getSize() - 1; i++) {
                Stop origin = getStopById(longestPath.getAt(i));
                Stop dest = getStopById(longestPath.getAt(i + 1));
                if (origin != null && dest != null) {
                    g2d.drawLine(origin.getX(), origin.getY(), dest.getX(), dest.getY());
                }
            }
        }

        // Dibujar vértices (paradas)
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 0; i < stops.getSize(); i++) {
            Stop stop = stops.getAt(i);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillOval(stop.getX() - 20, stop.getY() - 20, 40, 40);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(stop.getX() - 20, stop.getY() - 20, 40, 40);
            g2d.drawString(stop.getName() + " (" + stop.getId() + ")", stop.getX() - 15, stop.getY() + 5);
        }
    }

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
