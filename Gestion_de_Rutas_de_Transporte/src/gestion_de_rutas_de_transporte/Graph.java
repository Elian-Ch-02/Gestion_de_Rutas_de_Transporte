// Graph.java (Modificada para aristas ponderadas, Dijkstra, ruta más larga y soporte para dibujo)
package gestion_de_rutas_de_transporte;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Clase que representa un grafo para modelar las conexiones entre paradas.
 * Utiliza listas de adyacencia implementadas con CustomLinkedList<Pair<vecino, peso>>.
 * Justificación: Los grafos son ideales para redes de transporte, permitiendo planificación de rutas eficiente con Dijkstra (corta) y backtracking (larga).
 * @author Elian
 */
public class Graph {
    public static final int MAX_STOPS = 100;
    public CustomLinkedList<Pair<Integer, Integer>>[] adjacency;
    private int numStops;

    @SuppressWarnings("unchecked")
    public Graph() {
        adjacency = new CustomLinkedList[MAX_STOPS];
        for (int i = 0; i < MAX_STOPS; i++) {
            adjacency[i] = new CustomLinkedList<>();
        }
        numStops = 0;
    }

    public void removeStop(int id) {
    if (id < 1 || id > MAX_STOPS) return;
    
    // Limpiar lista de adyacencia del vértice eliminado
    adjacency[id - 1].clear();
    
    // Eliminar todas las aristas que apuntaban a este vértice desde otros
    for (int i = 0; i < MAX_STOPS; i++) {
        Node<Pair<Integer, Integer>> current = adjacency[i].head;
        Node<Pair<Integer, Integer>> previous = null;
        
        while (current != null) {
            if (current.getData().first == id) {
                if (previous == null) {
                    adjacency[i].head = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                break; // Solo puede haber una arista por par
            }
            previous = current;
            current = current.getNext();
        }
    }
}
    
    public void addStop(int id) {
        if (id > 0 && id <= MAX_STOPS) {
            numStops = Math.max(numStops, id);
        }
    }

    public void addEdge(int from, int to, int peso) {
    if (from < 1 || from > numStops || to < 1 || to > numStops || from == to) {
        return;
    }

    // Verificar si ya existe la arista from → to
    Node<Pair<Integer, Integer>> current = adjacency[from - 1].head;
    while (current != null) {
        if (current.getData().first == to) {
            // Ya existe: opcionalmente actualizar al menor peso (por si acaso)
            if (peso < current.getData().second) {
                current.getData().second = peso;
                // También actualizar la dirección inversa
                updateReverseWeight(to, from, peso);
            }
            return; // No añadir duplicada
        }
        current = current.getNext();
    }

    // No existe → añadir en ambas direcciones
    adjacency[from - 1].add(new Pair<>(to, peso));
    adjacency[to - 1].add(new Pair<>(from, peso));
}

private void updateReverseWeight(int from, int to, int newWeight) {
    Node<Pair<Integer, Integer>> current = adjacency[from - 1].head;
    while (current != null) {
        if (current.getData().first == to) {
            current.getData().second = newWeight;
            break;
        }
        current = current.getNext();
    }
}

    // Ruta más corta usando Dijkstra
    public CustomLinkedList<Integer> findPath(int start, int end) {
        if (start < 1 || start > numStops || end < 1 || end > numStops) {
            return new CustomLinkedList<>();
        }

        int[] dist = new int[MAX_STOPS];
        int[] parent = new int[MAX_STOPS];
        for (int i = 0; i < MAX_STOPS; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dist[start - 1] = 0;

        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.second - b.second); // <vertex, dist>
        pq.add(new Pair<>(start, 0));

        while (!pq.isEmpty()) {
            Pair<Integer, Integer> top = pq.poll();
            int current = top.first;
            if (top.second > dist[current - 1]) continue;

            Node<Pair<Integer, Integer>> neighbor = adjacency[current - 1].head;
            while (neighbor != null) {
                Pair<Integer, Integer> neigh = neighbor.getData();
                int neighId = neigh.first;
                int weight = neigh.second;
                int newDist = dist[current - 1] + weight;
                if (newDist < dist[neighId - 1]) {
                    dist[neighId - 1] = newDist;
                    parent[neighId - 1] = current;
                    pq.add(new Pair<>(neighId, newDist));
                }
                neighbor = neighbor.getNext();
            }
        }

        CustomLinkedList<Integer> path = new CustomLinkedList<>();
        int current = end;
        while (current != -1) {
            path.add(current);
            current = parent[current - 1];
        }

        if (path.getAt(path.getSize() - 1) != start) return new CustomLinkedList<>();

        CustomLinkedList<Integer> reversedPath = new CustomLinkedList<>();
        for (int i = path.getSize() - 1; i >= 0; i--) {
            reversedPath.add(path.getAt(i));
        }
        return reversedPath;
    }

    // Ruta más larga usando backtracking (asumiendo grafo acíclico)
    public CustomLinkedList<Integer> longestPath(int origen, int destino) {
        CustomLinkedList<Integer> maxPath = new CustomLinkedList<>();
        int[] maxWeight = {Integer.MIN_VALUE};
        CustomLinkedList<Integer> path = new CustomLinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        findLongestPath(origen, destino, visited, path, 0, maxPath, maxWeight);
        return maxPath;
    }

    private void findLongestPath(int current, int dest, HashSet<Integer> visited, CustomLinkedList<Integer> path, int currentWeight,
                                 CustomLinkedList<Integer> maxPath, int[] maxWeight) {
        visited.add(current);
        path.add(current);

        if (current == dest) {
            if (currentWeight > maxWeight[0]) {
                maxWeight[0] = currentWeight;
                maxPath.clear();
                for (int i = 0; i < path.getSize(); i++) {
                    maxPath.add(path.getAt(i));
                }
            }
            visited.remove(current);
            path.remove(current);
            return;
        }

        Node<Pair<Integer, Integer>> neighbor = adjacency[current - 1].head;
        while (neighbor != null) {
            Pair<Integer, Integer> neigh = neighbor.getData();
            int neighId = neigh.first;
            int weight = neigh.second;
            if (!visited.contains(neighId)) {
                findLongestPath(neighId, dest, visited, path, currentWeight + weight, maxPath, maxWeight);
            }
            neighbor = neighbor.getNext();
        }

        visited.remove(current);
        path.remove(current);
    }
}
