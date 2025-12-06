/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion_de_rutas_de_transporte;

/**
 * Clase que representa un horario en el sistema.
 * Incluye identificador, id de ruta asociada y hora.
 * @author Elian
 */
public class Schedule {
    private int id;
    private int routeId;
    private String time;

    /**
     * Constructor de la clase Schedule.
     * @param id Identificador único del horario.
     * @param routeId Identificador de la ruta asociada.
     * @param time Hora del horario (formato HH:MM).
     */
    public Schedule(int id, int routeId, String time) {
        this.id = id;
        this.routeId = routeId;
        this.time = time;
    }

    /**
     * Obtiene el identificador del horario.
     * @return id del horario.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del horario.
     * @param id Nuevo identificador.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el id de la ruta asociada.
     * @return id de la ruta.
     */
    public int getRouteId() {
        return routeId;
    }

    /**
     * Establece el id de la ruta asociada.
     * @param routeId Nuevo id de ruta.
     */
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    /**
     * Obtiene la hora del horario.
     * @return Hora del horario.
     */
    public String getTime() {
        return time;
    }

    /**
     * Establece la hora del horario.
     * @param time Nueva hora.
     */
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Schedule{" + "id=" + id + ", routeId=" + routeId + ", time='" + time + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Schedule schedule = (Schedule) obj;
        return id == schedule.id;
    }
}
