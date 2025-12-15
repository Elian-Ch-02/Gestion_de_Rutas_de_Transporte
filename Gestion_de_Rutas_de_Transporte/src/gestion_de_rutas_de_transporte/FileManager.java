// FileManager.java (Modificada para guardar/cargar pesos en aristas)
package gestion_de_rutas_de_transporte;


import java.io.*;
import java.util.Scanner;

/**
 * Clase para manejar la persistencia de datos en archivo de texto.
 * Guarda y carga paradas, rutas, horarios y aristas del grafo con pesos.
 * @author Elian
 */
public class FileManager {
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

            // Guardar rutas
            writer.println("ROUTES");
            Node<Route> routeNode = routes.head;
            while (routeNode != null) {
                Route route = routeNode.getData();
                writer.print(route.getId() + "," + route.getName() + ",STOPS:");
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
                Schedule sch = scheduleNode.getData();
                writer.println(sch.getId() + "," + sch.getRouteId() + "," + sch.getTime());
                scheduleNode = scheduleNode.getNext();
            }

            // Guardar aristas con pesos
            writer.println("EDGES");
            for (int i = 0; i < Graph.MAX_STOPS; i++) {
                Node<Pair<Integer, Integer>> neighbor = graph.adjacency[i].head;
                while (neighbor != null) {
                    Pair<Integer, Integer> pair = neighbor.getData();
                    if (i + 1 < pair.first) { // Evitar duplicados
                        writer.println((i + 1) + "," + pair.first + "," + pair.second);
                    }
                    neighbor = neighbor.getNext();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
        }
    }

    public static void load(String filename, CustomLinkedList<Stop> stops, CustomLinkedList<Route> routes,
                            CustomLinkedList<Schedule> schedules, Graph graph) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            String section = "";
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
                    Route route = new Route(id, name);
                    String stopsStr = parts[2].substring(6); // STOPS:
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