/**
 * Clase que representa un grafo para modelar las conexiones entre paradas.
 * Utiliza listas de adyacencia implementadas con CustomLinkedList<Pair<vecino, peso>> para aristas ponderadas.
 * Soporta adición/eliminación de vértices y aristas, rutas más cortas (Dijkstra) y más largas (backtracking).
 * Compone un arreglo de listas de adyacencia y constantes para límites.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.model;


import gestion_de_rutas_de_transporte.utils.CustomLinkedList;
import java.util.HashSet;
import java.util.PriorityQueue;



public class Graph {
    public static final int MAX_STOPS = 100;
    public CustomLinkedList<Pair<Integer, Integer>>[] adjacency;
    private int numStops;

    /**
     * Constructor del grafo. Inicializa el arreglo de listas de adyacencia.
     */
    @SuppressWarnings("unchecked")
    public Graph() {
        adjacency = new CustomLinkedList[MAX_STOPS];
        for (int i = 0; i < MAX_STOPS; i++) {
            adjacency[i] = new CustomLinkedList<>();
        }
        numStops = 0;
    }

    /**
     * Elimina una parada (vértice) y todas las aristas conectadas a ella.
     * @param id ID de la parada a eliminar.
     */
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
    
    /**
     * Agrega una parada (vértice) al grafo.
     * @param id ID de la parada.
     */
    public void addStop(int id) {
        if (id > 0 && id <= MAX_STOPS) {
            numStops = Math.max(numStops, id);
        }
    }

    /**
     * Agrega una arista ponderada no dirigida entre dos paradas, evitando duplicados.
     * @param from ID de origen.
     * @param to ID de destino.
     * @param peso Peso de la arista.
     */
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

    
/**
* Actualiza el peso de la arista inversa.
* @param from ID de origen inverso.
* @param to ID de destino inverso.
* @param newWeight Nuevo peso.
*/
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
    /**
     * Encuentra la ruta más corta entre dos paradas usando Dijkstra.
     * @param start ID de inicio.
     * @param end ID de fin.
     * @return Lista de IDs en la ruta o lista vacía si no hay ruta.
     */
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

    /**
     * Encuentra la ruta más larga entre origen y destino usando backtracking (asumiendo grafo acíclico).
     * @param origen ID de origen.
     * @param destino ID de destino.
     * @return Lista de IDs en la ruta más larga.
     */
    public CustomLinkedList<Integer> longestPath(int origen, int destino) {
        CustomLinkedList<Integer> maxPath = new CustomLinkedList<>();
        int[] maxWeight = {Integer.MIN_VALUE};
        CustomLinkedList<Integer> path = new CustomLinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        findLongestPath(origen, destino, visited, path, 0, maxPath, maxWeight);
        return maxPath;
    }

    /**
     * Método recursivo auxiliar para encontrar la ruta más larga mediante backtracking.
     * @param current Nodo actual.
     * @param dest Destino.
     * @param visited Conjunto de visitados.
     * @param path Ruta actual.
     * @param currentWeight Peso actual.
     * @param maxPath Ruta máxima encontrada.
     * @param maxWeight Peso máximo.
     */
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

    /**
     * Encuentra la ruta más corta (pendiente de implementación).
     * @param origen ID de origen.
     * @param destino ID de destino.
     * @return Lista de IDs en la ruta.
     */
    public CustomLinkedList<Integer> shortestPath(int origen, int destino) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
