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
     * @param coord Coordenada del disparo
     * 
     * @throws IllegalArgumentException Cuando la fila (row) o columna (col) de la
     *                                  coordenada (coord) se encuentran fuera de
     *                                  los límites del tablero
     */
    void shoot(Coordinate coordinate);

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
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return Estado de la celda correspondiente
     */
    byte getCellState(int row, int col);

    /**
     * Establece una celda determinada al nuevo estado
     * 
     * @param row   Fila del tablero
     * @param col   Columna del tablero
     * @param state Nuevo estado de la celda
     */
    void setCellState(int row, int col, byte state);

    /**
     * Añade un barco en la posición determinada
     * 
     * @param coordinate Coordenada inicial del barco
     * @param ship       Barco a añadir
     */
    void putShip(Coordinate coordinate, byte ship);

}
