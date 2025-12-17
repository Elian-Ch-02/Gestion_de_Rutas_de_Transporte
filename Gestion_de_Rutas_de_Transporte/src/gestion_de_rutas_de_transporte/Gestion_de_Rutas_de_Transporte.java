// Gestion_de_Rutas.java (Modificada para incluir panel de dibujo, rutas ponderadas, ruta larga y repintado)
package gestion_de_rutas_de_transporte;

import gestion_de_rutas_de_transporte.FileManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Clase principal que implementa la interfaz gráfica de usuario (GUI) utilizando JFrame.
 * Maneja las operaciones del sistema de gestión de rutas de transporte con visualización.
 * @author Elian
 */
public class  Gestion_de_Rutas_de_Transporte extends JFrame {
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

    public Gestion_de_Rutas_de_Transporte(){
        // Cargar datos
        FileManager.load(filename, stops, routes, schedules, graph);
        updateNextIds();

        setTitle("Sistema de Gestion de Rutas de Transporte");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de dibujo
        panelDibujo = new PanelDibujo(graph, stops);
        add(panelDibujo, BorderLayout.CENTER);

        // Área de salida (ahora a la derecha)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(300, 0));
        add(scrollPane, BorderLayout.EAST);

        // Panel de botones (abajo)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3));

        addStopButton = new JButton("Agregar Parada");
        addStopButton.addActionListener(new AddStopListener());
        buttonPanel.add(addStopButton);

        addRouteButton = new JButton("Agregar Ruta");
        addRouteButton.addActionListener(new AddRouteListener());
        buttonPanel.add(addRouteButton);

        addScheduleButton = new JButton("Agregar Horario");
        addScheduleButton.addActionListener(new AddScheduleListener());
        buttonPanel.add(addScheduleButton);

        showAllButton = new JButton("Mostrar Todo");
        showAllButton.addActionListener(e -> showAll());
        buttonPanel.add(showAllButton);

        searchButton = new JButton("Buscar");
        searchButton.addActionListener(new SearchListener());
        buttonPanel.add(searchButton);

        deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new DeleteListener());
        buttonPanel.add(deleteButton);

        planRouteButton = new JButton("Planificar Recorrido");
        planRouteButton.addActionListener(new PlanRouteListener());
        buttonPanel.add(planRouteButton);

        sortButton = new JButton("Ordenar Paradas");
        sortButton.addActionListener(e -> sortStops());
        buttonPanel.add(sortButton);

        exitButton = new JButton("Salir y Guardar");
        exitButton.addActionListener(e -> exitAndSave());
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateNextIds() {
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

    private void addStop() {
        String name = JOptionPane.showInputDialog(this, "Nombre de la parada:");
        if (name != null && !name.isEmpty()) {
            Random rand = new Random();
            int x = rand.nextInt(500) + 50;
            int y = rand.nextInt(300) + 50;
            Stop stop = new Stop(nextStopId++, name, x, y);
            stops.add(stop);
            graph.addStop(stop.getId());
            outputArea.append("Parada agregada: " + stop + "\n");
            panelDibujo.repaint();
        }
    }

    private void addRoute() {
        String name = JOptionPane.showInputDialog(this, "Nombre de la ruta:");
        if (name != null && !name.isEmpty()) {
            Route route = new Route(nextRouteId++, name);
            String stopIdsStr = JOptionPane.showInputDialog(this, "IDs de paradas separados por coma (ej. 1,2,3):");
            if (stopIdsStr != null) {
                String[] ids = stopIdsStr.split(",");
                Random rand = new Random();
                for (String idStr : ids) {
                    try {
                        int stopId = Integer.parseInt(idStr.trim());
                        if (stops.search(stopId) != null) {
                            route.getStopIds().add(stopId);
                            if (route.getStopIds().getSize() > 1) {
                                int prev = route.getStopIds().getAt(route.getStopIds().getSize() - 2);
                                int peso = rand.nextInt(10) + 1; // Peso aleatorio 1-10
                                graph.addEdge(prev, stopId, peso);
                            }
                        } else {
                            outputArea.append("Parada " + stopId + " no encontrada.\n");
                        }
                    } catch (NumberFormatException ex) {
                        outputArea.append("ID invalido: " + idStr + "\n");
                    }
                }
                routes.add(route);
                outputArea.append("Ruta agregada: " + route + "\n");
                panelDibujo.repaint();
            }
        }
    }

    private void addSchedule() {
        String routeIdStr = JOptionPane.showInputDialog(this, "ID de ruta:");
        try {
            int routeId = Integer.parseInt(routeIdStr);
            if (routes.search(routeId) != null) {
                String time = JOptionPane.showInputDialog(this, "Hora (ej. 08:00):");
                if (time != null) {
                    Schedule sch = new Schedule(nextScheduleId++, routeId, time);
                    schedules.add(sch);
                    outputArea.append("Horario agregado: " + sch + "\n");
                }
            } else {
                outputArea.append("Ruta no encontrada.\n");
            }
        } catch (NumberFormatException ex) {
            outputArea.append("ID invalido.\n");
        }
    }

    private void showAll() {
        outputArea.append("Paradas:\n");
        Node<Stop> stopNode = stops.head;
        while (stopNode != null) {
            outputArea.append(stopNode.getData() + "\n");
            stopNode = stopNode.getNext();
        }
        outputArea.append("Rutas:\n");
        Node<Route> routeNode = routes.head;
        while (routeNode != null) {
            outputArea.append(routeNode.getData() + "\n");
            routeNode = routeNode.getNext();
        }
        outputArea.append("Horarios:\n");
        Node<Schedule> schNode = schedules.head;
        while (schNode != null) {
            outputArea.append(schNode.getData() + "\n");
            schNode = schNode.getNext();
        }
    }

    private void search() {
       String[] options = {"Parada por ID", "Ruta por ID", "Horario por ID"};
        String choice = (String) JOptionPane.showInputDialog(this, "Seleccionar tipo de busqueda:", "Buscar", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice != null) {
            String idStr = JOptionPane.showInputDialog(this, "ID:");
            try {
                int id = Integer.parseInt(idStr);
                Object result = null;
                if (choice.equals("Parada por ID")) result = stops.search(id);
                else if (choice.equals("Ruta por ID")) result = routes.search(id);
                else if (choice.equals("Horario por ID")) result = schedules.search(id);
                outputArea.append(result != null ? result + "\n" : "No encontrado.\n");
            } catch (NumberFormatException ex) {
                outputArea.append("ID invalido.\n");
            }
        }
    }
    

    private void delete() {
    String[] options = {"Eliminar Parada por ID", "Eliminar Ruta por ID", "Eliminar Horario por ID"};
    String choice = (String) JOptionPane.showInputDialog(
        this, "Seleccionar tipo de eliminación:", "Eliminar",
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (choice == null) return; // Canceló

    String idStr = JOptionPane.showInputDialog(this, "ID a eliminar:");
    if (idStr == null || idStr.trim().isEmpty()) return;

    try {
        int id = Integer.parseInt(idStr);
        boolean removed = false;

        if (choice.equals("Eliminar Parada por ID")) {
            // Usamos coordenadas dummy (0,0) porque solo importa el id para equals()
            removed = stops.remove(new Stop(id, "", 0, 0));
            if (removed) {
                // Opcional pero muy recomendable: limpiar las aristas del grafo
                graph.removeStop(id); // Necesitas añadir este método en Graph (ver abajo)
            }
        } 
        else if (choice.equals("Eliminar Ruta por ID")) {
            removed = routes.remove(new Route(id, ""));
        } 
        else if (choice.equals("Eliminar Horario por ID")) {
            removed = schedules.remove(new Schedule(id, 0, ""));
        }

        outputArea.append(removed ? "Elemento eliminado correctamente.\n" 
                                  : "No se encontró el elemento con ID " + id + ".\n");
        
        panelDibujo.repaint(); // Siempre repintar después de eliminar

    } catch (NumberFormatException ex) {
        outputArea.append("ID inválido. Debe ser un número.\n");
    }
    }

    private void planRoute() {
        String startStr = JOptionPane.showInputDialog(this, "ID de parada inicio:");
        String endStr = JOptionPane.showInputDialog(this, "ID de parada fin:");
        try {
            int start = Integer.parseInt(startStr);
            int end = Integer.parseInt(endStr);
            CustomLinkedList<Integer> shortPath = graph.findPath(start, end);
            CustomLinkedList<Integer> longPath = graph.longestPath(start, end);
            panelDibujo.setShortestPath(shortPath);
            panelDibujo.setLongestPath(longPath);

            if (shortPath.getSize() == 0) {
                outputArea.append("No hay ruta corta.\n");
            } else {
                outputArea.append("Recorrido corto: ");
                for (int i = 0; i < shortPath.getSize(); i++) {
                    outputArea.append(shortPath.getAt(i) + (i < shortPath.getSize() - 1 ? " -> " : "\n"));
                }
            }

            if (longPath.getSize() == 0) {
                outputArea.append("No hay ruta larga.\n");
            } else {
                outputArea.append("Recorrido largo: ");
                for (int i = 0; i < longPath.getSize(); i++) {
                    outputArea.append(longPath.getAt(i) + (i < longPath.getSize() - 1 ? " -> " : "\n"));
                }
            }
        } catch (NumberFormatException ex) {
            outputArea.append("IDs invalidos.\n");
        }
    }

    private void sortStops() {
        Sorter.bubbleSort(stops);
        outputArea.append("Paradas ordenadas por nombre.\n");
        panelDibujo.repaint();
    }

    private void exitAndSave() {
        FileManager.save(filename, stops, routes, schedules, graph);
        System.exit(0);
    }

    // Listeners de acciones
    private class AddStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addStop();
        }
    }

    private class AddRouteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addRoute();
        }
    }

    private class AddScheduleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            addSchedule();
        }
    }

    private class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            search();
        }
    }

    private class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            delete();
        }
    }

    private class PlanRouteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            planRoute();
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gestion_de_Rutas_de_Transporte());
    }
}
