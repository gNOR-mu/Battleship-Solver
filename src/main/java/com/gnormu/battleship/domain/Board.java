package com.gnormu.battleship.domain;

public interface Board {
    /**
     * Limpia completamente el tablero, restableciendolo a sus valores iniciales
     */
    void clear();

    /**
     * Realiza un disparo en la coordenada indicada, actualizando el estado de la
     * celda y restando vida al barco en caso de impacto.
     * 
     * @param coordinate Coordenada lineal del disparo
     * 
     * @throws IllegalArgumentException Cuando la coordenada se encuentra fuera de
     *                                  los límites del tablero
     */
    void shoot(int coordinate);

    /**
     * Verifica si el juego ha terminado
     * 
     * @return <code>true</code> si el juego ha terminado, <code>false</code> en
     *         caso contrario
     */
    boolean isGameOver();

    /**
     * Obtiene la representación de la celda en la coordenada determinada
     * 
     * @param coordinate Coordenada lineal de la celda
     * @return Estado de la celda correspondiente
     */
    byte getCellState(int coordinate);

    /**
     * Establece una celda determinada al nuevo estado
     * 
     * @param coordinate Coordenada lineal de la celda
     * @param state Nuevo estado de la celda
     */
    void setCellState(int coordinate, byte state);

    /**
     * Añade un barco en la posición determinada
     * 
     * @param coordinate Coordenada lineal inicial del barco
     * @param ship       Barco a añadir
     */
    void putShip(int coordinate, byte ship);

}
