package com.gnormu.battleship.domain;

public interface Board {
    /**
     * Limpia completamente el tablero, restableciendolo a sus valores iniciales
     */
    void reset();

    /**
     * Establece una celda determinada al nuevo estado
     * 
     * @param coordinate Coordenada lineal de la celda
     * @param state      Nuevo estado de la celda
     */
    void setCellState(byte coordinate, byte state);

    /**
     * Añade un barco en la posición determinada
     * 
     * @param coordinate Coordenada lineal inicial del barco
     * @param ship       Barco a añadir
     */
    void putShip(byte coordinate, byte ship);

    /**
     * Realiza un disparo en la coordenada indicada, actualizando el estado de la
     * celda y restando vida al barco en caso de impacto.
     * 
     * @param coordinate Coordenada lineal del disparo
     * 
     * @return Contenido inicial de la celda, puede {@link CellContent#WATER} si es
     *         agua, un número > 0 si es un barco o un número negativo,
     *         correspondiente al negativo de un barco u {@link CellContent#MISS}.
     *         En el caso de un barco invertido se considera una multiplicación por
     *         menos uno: ejemplo Barco 1 pasa a ser -1 en las casillas y se retorna
     *         1.
     */
    byte shoot(byte coordinate);

    /**
     * Obtiene la representación de la celda en la coordenada determinada
     * 
     * @param coordinate Coordenada lineal de la celda
     * @return Estado de la celda correspondiente
     * 
     * @implNote Un valor positivo indica un barco no impactado
     */
    byte getCellState(byte coordinate);

    /**
     * Verifica si el juego ha terminado
     * 
     * @return <code>true</code> si el juego ha terminado, <code>false</code> en
     *         caso contrario
     */
    boolean isGameOver();

    /**
     * Indica si un barco ha sido hundido
     * 
     * @param ship Identificador del barco
     * @return <code>true</code> en caso de que el barco ya fue hundido (vida == 0),
     *         <code>false</code> en caso contrario (vida > 0)
     */
    boolean isShipSunk(byte ship);

    /**
     * @return Nombre de la representación del tablero
     */
    String getBoardName();

}
