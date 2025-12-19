/**
 * Clase para manejar la persistencia de datos en archivo de texto.
 * Guarda y carga paradas (con coordenadas), rutas (con colores y paradas), horarios y aristas del grafo con pesos.
 * Utiliza secciones en el archivo para organizar los datos.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.utils;



import gestion_de_rutas_de_transporte.model.Node;
import gestion_de_rutas_de_transporte.model.Graph;
import gestion_de_rutas_de_transporte.model.Pair;
import gestion_de_rutas_de_transporte.model.Schedule;
import gestion_de_rutas_de_transporte.model.Route;
import gestion_de_rutas_de_transporte.model.Stop;
import java.awt.Color;
import java.io.*;
import java.util.Scanner;

public class FileManager {
    
    /**
     * Guarda todas las estructuras de datos en un archivo.
     * @param filename Nombre del archivo.
     * @param stops Lista de paradas.
     * @param routes Lista de rutas.
     * @param schedules Lista de horarios.
     * @param graph Grafo con aristas.
     */
    public static void save(String filename, CustomLinkedList<Stop> stops, CustomLinkedList<Route> routes,
                            CustomLinkedList<Schedule> schedules, Graph graph) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Guardar paradas (ahora con x,y)
            writer.println("STOPS");
            Node<Stop> stopNode = stops.head;
            while (stopNode != null) {
                Stop stop = stopNode.getData();
                writer.println(stop.getId() + "," + stop.getName() + "," + stop.getX() + "," + stop.getY());
                stopNode = stopNode.getNext();
            }

            // Guardar rutas (agregar color como RGB)
            writer.println("ROUTES");
            Node<Route> routeNode = routes.head;
            while (routeNode != null) {
                Route route = routeNode.getData();
                Color color = route.getColor();
                int r = color != null ? color.getRed() : 0;
                int g = color != null ? color.getGreen() : 0;
                int b = color != null ? color.getBlue() : 0;
                writer.print(route.getId() + "," + route.getName() + "," + r + ";" + g + ";" + b + ",STOPS:");
                Node<Integer> stopIdNode = route.getStopIds().head;
                while (stopIdNode != null) {
                    writer.print(stopIdNode.getData() + ";");
                    stopIdNode = stopIdNode.getNext();
                }
                writer.println();
                routeNode = routeNode.getNext();
            }

            // Guardar horarios
            writer.println("SCHEDULES");
            Node<Schedule> scheduleNode = schedules.head;
            while (scheduleNode != null) {
                Schedule schedule = scheduleNode.getData();
                writer.println(schedule.getId() + "," + schedule.getRouteId() + "," + schedule.getTime());
                scheduleNode = scheduleNode.getNext();
            }

            // Guardar aristas (edges) con pesos
            writer.println("EDGES");
            for (int i = 0; i < Graph.MAX_STOPS; i++) {
                Node<Pair<Integer, Integer>> edgeNode = graph.adjacency[i].head;
                while (edgeNode != null) {
                    Pair<Integer, Integer> edge = edgeNode.getData();
                    writer.println((i + 1) + "," + edge.first + "," + edge.second);
                    edgeNode = edgeNode.getNext();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
        }
    }

    /**
     * Carga todas las estructuras de datos desde un archivo.
     * @param filename Nombre del archivo.
     * @param stops Lista de paradas a llenar.
     * @param routes Lista de rutas a llenar.
     * @param schedules Lista de horarios a llenar.
     * @param graph Grafo a llenar con aristas.
     */
    public static void load(String filename, CustomLinkedList<Stop> stops, CustomLinkedList<Route> routes,
                            CustomLinkedList<Schedule> schedules, Graph graph) {
        String section = "";
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                if (line.equals("STOPS") || line.equals("ROUTES") || line.equals("SCHEDULES") || line.equals("EDGES")) {
                    section = line;
                    continue;
                }
                String[] parts = line.split(",");
                if (section.equals("STOPS")) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int x = Integer.parseInt(parts[2]);
                    int y = Integer.parseInt(parts[3]);
                    stops.add(new Stop(id, name, x, y));
                    graph.addStop(id);
                } else if (section.equals("ROUTES")) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String colorStr = parts[2]; // r;g;b
                    String[] rgb = colorStr.split(";");
                    int r = Integer.parseInt(rgb[0]);
                    int g = Integer.parseInt(rgb[1]);
                    int b = Integer.parseInt(rgb[2]);
                    Route route = new Route(id, name);
                    route.setColor(new Color(r, g, b));
                    String stopsStr = parts[3].substring(6); // STOPS:
                    String[] stopIds = stopsStr.split(";");
                    for (String sid : stopIds) {
                        if (!sid.isEmpty()) {
                            route.getStopIds().add(Integer.parseInt(sid));
                        }
                    }
                    routes.add(route);
                } else if (section.equals("SCHEDULES")) {
                    int id = Integer.parseInt(parts[0]);
                    int routeId = Integer.parseInt(parts[1]);
                    String time = parts[2];
                    schedules.add(new Schedule(id, routeId, time));
                } else if (section.equals("EDGES")) {
                    int from = Integer.parseInt(parts[0]);
                    int to = Integer.parseInt(parts[1]);
                    int peso = Integer.parseInt(parts[2]);
                    graph.addEdge(from, to, peso);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, iniciando en blanco.");
        } catch (Exception e) {
            System.out.println("Error al cargar archivo: " + e.getMessage());
        }
    }
}