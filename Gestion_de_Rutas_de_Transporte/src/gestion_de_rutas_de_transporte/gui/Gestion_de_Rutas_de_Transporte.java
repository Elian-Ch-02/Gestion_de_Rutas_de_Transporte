/**
 * Clase principal que implementa la interfaz gráfica de usuario (GUI) utilizando JFrame.
 * Maneja las operaciones del sistema de gestión de rutas de transporte con visualización gráfica del grafo,
 * lista de rutas y panel de operaciones. Compone elementos como listas enlazadas para paradas, rutas y horarios,
 * un grafo para modelar conexiones, y componentes Swing para la interfaz.
 * @author Elian
 */
package gestion_de_rutas_de_transporte.gui;

import gestion_de_rutas_de_transporte.utils.Sorter;
import gestion_de_rutas_de_transporte.utils.CustomLinkedList;
import gestion_de_rutas_de_transporte.model.Node;
import gestion_de_rutas_de_transporte.model.Graph;
import gestion_de_rutas_de_transporte.model.Schedule;
import gestion_de_rutas_de_transporte.model.Route;
import gestion_de_rutas_de_transporte.model.Stop;
import gestion_de_rutas_de_transporte.utils.FileManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Random;


public class Gestion_de_Rutas_de_Transporte extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable routeTable;  // Para la lista de rutas
    private CustomLinkedList<Stop> stops = new CustomLinkedList<>();
    private CustomLinkedList<Route> routes = new CustomLinkedList<>();
    private CustomLinkedList<Schedule> schedules = new CustomLinkedList<>();
    private Graph graph = new Graph();
    private String filename = "data.txt";
    private int nextStopId = 1;
    private int nextRouteId = 1;
    private int nextScheduleId = 1;

    private JTextArea outputArea;
    private PanelDibujo panelDibujo;
    private JButton addStopButton, addRouteButton, addScheduleButton, showAllButton, searchButton, deleteButton, planRouteButton, sortButton, exitButton;

    /**
     * Constructor de la clase principal. Inicializa la interfaz gráfica, carga datos desde archivo
     * y configura listeners para los botones de operaciones.
     */
    public Gestion_de_Rutas_de_Transporte() {
        setTitle("Sistema de Gestión de Rutas de Transporte");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear componentes GUI primero
        tabbedPane = new JTabbedPane();

        // Pestaña 1: Visualización de Grafo (con scroll)
        panelDibujo = new PanelDibujo(graph, stops, routes);
        JScrollPane graphScroll = new JScrollPane(panelDibujo);
        panelDibujo.setPreferredSize(new Dimension(1200, 800));  // Tamaño grande para grafo completo
        tabbedPane.addTab("Visualización de Grafo", graphScroll);

        // Pestaña 2: Lista de Rutas (con tabla)
        JPanel routesPanel = new JPanel(new BorderLayout());
        routeTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(routeTable);
        routesPanel.add(tableScroll, BorderLayout.CENTER);
        tabbedPane.addTab("Lista de Rutas", routesPanel);

        // Pestaña 3: Operaciones (botones en vertical con scroll)
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));  // Vertical
        addStopButton = new JButton("Agregar Parada");
        addRouteButton = new JButton("Agregar Ruta");
        addScheduleButton = new JButton("Agregar Horario");
        showAllButton = new JButton("Mostrar Todo");
        searchButton = new JButton("Buscar");
        deleteButton = new JButton("Eliminar");
        planRouteButton = new JButton("Planificar Recorrido");
        sortButton = new JButton("Ordenar Paradas");
        exitButton = new JButton("Salir y Guardar");
        buttonPanel.add(addStopButton);
        buttonPanel.add(addRouteButton);
        buttonPanel.add(addScheduleButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(planRouteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(exitButton);
        tabbedPane.addTab("Operaciones", new JScrollPane(buttonPanel));

        // Área de salida
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        // Usar JSplitPane para dividir la pantalla verticalmente (mitad superior: tabs, mitad inferior: output)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, tabbedPane, new JScrollPane(outputArea));
        splitPane.setResizeWeight(0.5);  // Divide en mitades
        splitPane.setDividerLocation(400);  // Posición inicial del divisor (ajustar según tamaño de ventana)

        add(splitPane, BorderLayout.CENTER);
        // Configurar listeners
        addStopButton.addActionListener(e -> addStop());
        addRouteButton.addActionListener(e -> addRoute());
        addScheduleButton.addActionListener(e -> addSchedule());
        showAllButton.addActionListener(e -> showAll());
        searchButton.addActionListener(e -> searchItem());
        deleteButton.addActionListener(e -> deleteItem());
        planRouteButton.addActionListener(e -> planRoute());
        sortButton.addActionListener(e -> sortStops());
        exitButton.addActionListener(e -> exitAndSave());

        // Ahora cargar datos y actualizar UI (después de que componentes existan)
        FileManager.load(filename, stops, routes, schedules, graph);
        updateNextIds();  // Esto llamará a initializeDefaultData() si es necesario
        updateRouteTable();  // Actualiza tabla
        panelDibujo.repaint();  // Repinta grafo

        setVisible(true);
    }

    private void updateNextIds() {
        if (stops.getSize() == 0 || routes.getSize() == 0 || schedules.getSize() == 0) {
        initializeDefaultData();
    } else {
        // Actualiza IDs basados en datos cargados
        for (int i = 0; i < stops.getSize(); i++) {
            nextStopId = Math.max(nextStopId, stops.getAt(i).getId() + 1);
        }
        for (int i = 0; i < routes.getSize(); i++) {
            nextRouteId = Math.max(nextRouteId, routes.getAt(i).getId() + 1);
        }
        for (int i = 0; i < schedules.getSize(); i++) {
            nextScheduleId = Math.max(nextScheduleId, schedules.getAt(i).getId() + 1);
        }
    }
    }

    private void initializeDefaultData() {
        Random random = new Random();
        // Paradas con posiciones espaciadas horizontalmente (x aumenta, y varía poco para líneas)
        stops.add(new Stop(nextStopId++, "Belén (Heredia)", 50, 50));
        stops.add(new Stop(nextStopId++, "Metroplaza/Multiplaza Rosa", 150, 60));
        stops.add(new Stop(nextStopId++, "DEMASA/Santa Rosa", 250, 70));
        stops.add(new Stop(nextStopId++, "PECOSA/Santo Reina", 350, 80));
        stops.add(new Stop(nextStopId++, "Estación del Atlántico", 450, 90));
        stops.add(new Stop(nextStopId++, "Ambos Mares", 550, 100));
        stops.add(new Stop(nextStopId++, "San Pedro (UCR)", 650, 110));
        stops.add(new Stop(nextStopId++, "Universidad Latina", 750, 120));
        stops.add(new Stop(nextStopId++, "Curridabat", 850, 130));
        stops.add(new Stop(nextStopId++, "Tres Rios", 950, 140));
        stops.add(new Stop(nextStopId++, "Pavas", 50, 200));
        stops.add(new Stop(nextStopId++, "Aya (Contraloria)", 150, 210));
        stops.add(new Stop(nextStopId++, "Sabana (La Salle/MAGI)", 250, 220));
        stops.add(new Stop(nextStopId++, "Sabana (Cementerio)", 350, 230));
        stops.add(new Stop(nextStopId++, "Estación del Pacífico", 450, 240));
        stops.add(new Stop(nextStopId++, "Plaza Gonzalez Viquez", 550, 250));
        stops.add(new Stop(nextStopId++, "Cartago (extensión)", 650, 260));
        stops.add(new Stop(nextStopId++, "Jack's", 750, 270));
        stops.add(new Stop(nextStopId++, "Procuraduría", 850, 280));

        // Rutas de ejemplo
      Route rutaRoja = new Route(nextRouteId++, "Belén - Tres Rios");
        rutaRoja.getStopIds().add(1); // Belén
        rutaRoja.getStopIds().add(2); // Metroplaza
        rutaRoja.getStopIds().add(3); // DEMASA
        rutaRoja.getStopIds().add(4); // PECOSA
        rutaRoja.getStopIds().add(5); // Estación del Atlántico
        rutaRoja.getStopIds().add(6); // Ambos Mares
        rutaRoja.getStopIds().add(7); // San Pedro
        rutaRoja.getStopIds().add(8); // Universidad Latina
        rutaRoja.getStopIds().add(9); // Curridabat
        rutaRoja.getStopIds().add(10); // Tres Rios
        rutaRoja.setColor(Color.RED);  // Rojo
        routes.add(rutaRoja);

        Route rutaNaranja = new Route(nextRouteId++, "Estación del Atlántico - Curridabat");
        rutaNaranja.getStopIds().add(5); // Estación del Atlántico
        rutaNaranja.getStopIds().add(18); // Jack's
        rutaNaranja.getStopIds().add(19); // Procuraduría
        rutaNaranja.getStopIds().add(9); // Curridabat
        rutaNaranja.setColor(Color.ORANGE);  // Naranja
        routes.add(rutaNaranja);

        Route rutaVerde = new Route(nextRouteId++, "Pavas - Cartago");
        rutaVerde.getStopIds().add(11); // Pavas
        rutaVerde.getStopIds().add(12); // Aya
        rutaVerde.getStopIds().add(13); // Sabana (La Salle)
        rutaVerde.getStopIds().add(14); // Sabana (Cementerio)
        rutaVerde.getStopIds().add(15); // Estación del Pacífico
        rutaVerde.getStopIds().add(16); // Plaza Gonzalez Viquez
        rutaVerde.getStopIds().add(17); // Cartago (extensión)
        rutaVerde.setColor(Color.GREEN);  // Verde
        routes.add(rutaVerde);

        // Conectar aristas en el grafo con pesos (ej. 5 minutos entre paradas consecutivas)
        connectRouteToGraph(rutaRoja, 5);
        connectRouteToGraph(rutaNaranja, 7);
        connectRouteToGraph(rutaVerde, 6);

        // Agregar horarios de ejemplo (opcional)
        schedules.add(new Schedule(nextScheduleId++, rutaRoja.getId(), "08:00"));
        schedules.add(new Schedule(nextScheduleId++, rutaNaranja.getId(), "09:00"));
        
    }

    private void updateRouteTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Ruta", "Nombre", "Inicio", "Fin", "Horarios"}, 0);
        Node<Route> routeNode = routes.getHead();
        while (routeNode != null) {
            Route route = routeNode.getData();
            String start = getStopNameById(route.getStopIds().getAt(0));
            String end = getStopNameById(route.getStopIds().getAt(route.getStopIds().getSize() - 1));
            String horarios = getSchedulesForRoute(route.getId());
            model.addRow(new Object[]{route.getId(), route.getName(), start, end, horarios});
            routeNode = routeNode.getNext();
        }
        routeTable.setModel(model);
    }

    private String getStopNameById(int id) {
        Node<Stop> stopNode = stops.getHead();
        while (stopNode != null) {
            Stop stop = stopNode.getData();
            if (stop.getId() == id) return stop.getName();
            stopNode = stopNode.getNext();
        }
        return "Desconocido";
    }

    private String getSchedulesForRoute(int routeId) {
        StringBuilder sb = new StringBuilder();
        Node<Schedule> schNode = schedules.getHead();
        while (schNode != null) {
            Schedule sch = schNode.getData();
            if (sch.getRouteId() == routeId) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(sch.getTime());
            }
            schNode = schNode.getNext();
        }
        return sb.toString();
    }

    // Método auxiliar para conectar paradas consecutivas en una ruta al grafo
    private void connectRouteToGraph(Route route, int defaultWeight) {
        CustomLinkedList<Integer> stopIds = route.getStopIds();
        for (int i = 0; i < stopIds.getSize() - 1; i++) {
            int from = stopIds.getAt(i);
            int to = stopIds.getAt(i + 1);
            graph.addEdge(from, to, defaultWeight);
        }
    }

    private void addStop() {
        String name = JOptionPane.showInputDialog(this, "Nombre de la parada:");
        if (name != null && !name.isEmpty()) {
            int x = Integer.parseInt(JOptionPane.showInputDialog(this, "Posición X:"));
            int y = Integer.parseInt(JOptionPane.showInputDialog(this, "Posición Y:"));
            Stop newStop = new Stop(nextStopId++, name, x, y);
            stops.add(newStop);
            graph.addStop(newStop.getId());
            outputArea.append("Parada agregada: " + newStop + "\n");
            updateRouteTable();
            panelDibujo.repaint();
        }
    }

    private void addRoute() {
        String name = JOptionPane.showInputDialog(this, "Nombre de la ruta:");
        if (name != null && !name.isEmpty()) {
            Route newRoute = new Route(nextRouteId++, name);
            String stopsInput = JOptionPane.showInputDialog(this, "IDs de paradas (separados por coma):");
            String[] stopIds = stopsInput.split(",");
            for (String sid : stopIds) {
                newRoute.getStopIds().add(Integer.parseInt(sid.trim()));
            }
          // Solicitar color para la nueva ruta
            Color color = JColorChooser.showDialog(this, "Elige color de la ruta", Color.BLACK);
            newRoute.setColor(color != null ? color : Color.BLACK);
            routes.add(newRoute);
            connectRouteToGraph(newRoute, 5);  // Peso por defecto
            outputArea.append("Ruta agregada: " + newRoute + "\n");
            updateRouteTable();
            panelDibujo.repaint();
        }
    }

    private void addSchedule() {
        int routeId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID de ruta:"));
        String time = JOptionPane.showInputDialog(this, "Hora (HH:MM):");
        Schedule newSchedule = new Schedule(nextScheduleId++, routeId, time);
        schedules.add(newSchedule);
        outputArea.append("Horario agregado: " + newSchedule + "\n");
        updateRouteTable();
    }
    /**
    * Muestra todos los elementos (paradas, rutas, horarios) en un diálogo con área de texto scrollable.
    */
    private void showAll() {
    showAll("");  // Llama a la versión con prefijo vacío
}

private void showAll(String prefix) {
    StringBuilder sb = new StringBuilder(prefix);
    sb.append("Paradas:\n");
    for (int i = 0; i < stops.getSize(); i++) {
        sb.append(stops.getAt(i)).append("\n");
    }
    sb.append("\nRutas:\n");
    for (int i = 0; i < routes.getSize(); i++) {
        sb.append(routes.getAt(i)).append("\n");
    }
    sb.append("\nHorarios:\n");
    for (int i = 0; i < schedules.getSize(); i++) {
        sb.append(schedules.getAt(i)).append("\n");
    }
    outputArea.setText(sb.toString());
    outputArea.setCaretPosition(0);  // Scroll al inicio
}

    /**
    * Busca un elemento (parada, ruta o horario) por tipo e ID, y muestra el resultado en el área de salida.
    */
    private void searchItem() {
        String type = JOptionPane.showInputDialog(this, "Buscar (parada/ruta/horario):");
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID:"));
        if ("parada".equalsIgnoreCase(type)) {
            Stop found = stops.search(id);
            outputArea.append(found != null ? found.toString() : "No encontrado");
        } else if ("ruta".equalsIgnoreCase(type)) {
            Route found = routes.search(id);
            outputArea.append(found != null ? found.toString() : "No encontrado");
        } else if ("horario".equalsIgnoreCase(type)) {
            Schedule found = schedules.search(id);
            outputArea.append(found != null ? found.toString() : "No encontrado");
        }
    }

    /**
    * Elimina un elemento (parada, ruta o horario) por tipo e ID, actualiza la tabla y repinta el panel.
    */
    private void deleteItem() {
        String type = JOptionPane.showInputDialog(this, "Eliminar (parada/ruta/horario):");
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID:"));
        if ("parada".equalsIgnoreCase(type)) {
            Stop toRemove = new Stop(id, "", 0, 0);
            if (stops.remove(toRemove)) {
                graph.removeStop(id);
                outputArea.append("Parada eliminada.\n");
            }
        } else if ("ruta".equalsIgnoreCase(type)) {
            Route toRemove = new Route(id, "");
            if (routes.remove(toRemove)) {
                outputArea.append("Ruta eliminada.\n");
            }
        } else if ("horario".equalsIgnoreCase(type)) {
            Schedule toRemove = new Schedule(id, 0, "");
            if (schedules.remove(toRemove)) {
                outputArea.append("Horario eliminado.\n");
            }
        }
        updateRouteTable();
        panelDibujo.repaint();
    }
    
    /**
    * Planifica una ruta entre origen y destino, muestra el resultado y resalta en el panel de dibujo.
    */
    private void planRoute() {
    int origen = Integer.parseInt(JOptionPane.showInputDialog(this, "ID de origen:"));
    int destino = Integer.parseInt(JOptionPane.showInputDialog(this, "ID de destino:"));
    String type = JOptionPane.showInputDialog(this, "Tipo (corta/larga/establecida):");  // Agrega opción "establecida"

    CustomLinkedList<Integer> path = null;
    
    if ("establecida".equalsIgnoreCase(type)) {
        // Buscar ruta establecida que contenga origen y destino
        path = findEstablishedRoutePath(origen, destino);
        if (path == null || path.getSize() == 0) {
            outputArea.append("No se encontró ruta establecida que conecte origen y destino.\n");
            return;
        }
    } else if ("corta".equalsIgnoreCase(type)) {
        path = graph.findPath(origen, destino);
    } else if ("larga".equalsIgnoreCase(type)) {
        path = graph.longestPath(origen, destino);
    }

    if (path != null && path.getSize() > 0) {
        StringBuilder sb = new StringBuilder("Ruta: ");
        for (int i = 0; i < path.getSize(); i++) {
            sb.append(getStopNameById(path.getAt(i))).append(" -> ");
        }
        outputArea.append(sb.toString() + "\n");
        panelDibujo.setSelectedRoutePath(path);  // Resalta en el panel
        panelDibujo.repaint();
    } else {
        outputArea.append("No se encontró ruta.\n");
    }
}
    
    private CustomLinkedList<Integer> findEstablishedRoutePath(int origen, int destino) {
    Node<Route> routeNode = routes.getHead();
    while (routeNode != null) {
        Route route = routeNode.getData();
        CustomLinkedList<Integer> stopIds = route.getStopIds();
        
        // Encontrar índices de origen y destino en la ruta
        int origenIndex = -1;
        int destinoIndex = -1;
        for (int i = 0; i < stopIds.getSize(); i++) {
            int stopId = stopIds.getAt(i);
            if (stopId == origen) origenIndex = i;
            if (stopId == destino) destinoIndex = i;
        }
        
        // Si ambos existen y origen antes de destino, extraer subpath
        if (origenIndex != -1 && destinoIndex != -1 && origenIndex < destinoIndex) {
            CustomLinkedList<Integer> subPath = new CustomLinkedList<>();
            for (int i = origenIndex; i <= destinoIndex; i++) {
                subPath.add(stopIds.getAt(i));
            }
            return subPath;
        }
        
        routeNode = routeNode.getNext();
    }
    return null;  // No encontrado
}

    /**
    * Ordena las paradas alfabéticamente y actualiza la visualización.
    */
    private void sortStops() {
     String criterio = JOptionPane.showInputDialog(this, "Ordenar por (nombre/id):");
    String message = "";
    if ("nombre".equalsIgnoreCase(criterio)) {
        Sorter.bubbleSort(stops, "nombre");
        message = "Paradas ordenadas alfabéticamente por nombre.\n";
    } else if ("id".equalsIgnoreCase(criterio)) {
        Sorter.bubbleSort(stops, "id");
        message = "Paradas ordenadas numéricamente por ID.\n";
    } else {
        outputArea.append("Criterio inválido. Usa 'nombre' o 'id'.\n");
        return;
    }
    showAll(message);  // Muestra el mensaje + toda la info
    panelDibujo.repaint();
}

    /**
    * Guarda los datos en archivo y cierra la aplicación.
    */
    private void exitAndSave() {
        FileManager.save(filename, stops, routes, schedules, graph);
        System.exit(0);
    }

    /**
    * Método principal para ejecutar la aplicación en el hilo de eventos de Swing.
    * @param args Argumentos de línea de comandos (no utilizados).
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gestion_de_Rutas_de_Transporte());
    }
}
