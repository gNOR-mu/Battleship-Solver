package com.gnormu.battleship.domain;

/**
 * Interfaz utilizada para construir posicionadores de flota
 */
public interface FleetPlacer {

    /**
     * Posiciona los barco es un tablero determinado
     * 
     * @param board Tablero {@link AbstractBoard} sobre el cual posicionar los
     *              barcos
     */
    void placeShips(Board board);

}
