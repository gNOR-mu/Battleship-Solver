package com.gnormu.battleship.domain;

import java.util.Arrays;

import com.gnormu.battleship.config.GameConfig;

/**
 * Clase abstracta para manejar los tableros de juego
 * 
 * @implNote Utilizar un arreglo primitivo para la vida de los barcos reduce el
 *           uso de memoria y tiempo de clonado al momento de utilizarlos en un
 *           tablero con cientos de miles de {@link Board} como se pretende
 *           usar.
 * 
 * @implNote Se ha optado utilizar por el patrón Flyweight para la
 *           instanciación de los barcos ({@link ShipType}), por lo que la
 *           lógica de manejo de vida del barco se ha delegado a la clase
 *           {@link AbstractBoard}
 * 
 */
public abstract class AbstractBoard implements Board {

    /** Arreglo primitivo para la vida de los barcos (patrón Flyweight) */
    protected final int[] shipHealths;

    /** Arreglo que representa la ubicación de los barcos en el tablero */
    protected final byte[] shipsGrid;

    /** Vida total de todos los barcos colocados en el tablero */
    protected int totalShipHealth;

    public AbstractBoard() {
        this.shipHealths = new int[ShipType.COUNT];
        this.shipsGrid = new byte[GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION];
    }

    /**
     * Valida que las coordenadas permanezcan estrictamente dentro del tablero
     * 
     * @param coordinate Coordenada a validar
     * 
     * @throws IllegalArgumentException Cuando la coordenada se encuentra fuera de
     *                                  los límites del tablero
     */
    protected void validateCoordinates(int coordinate) {
        if (coordinate < 0 || coordinate >= GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION) {
            throw new IllegalArgumentException(
                    "Disparo fuera de los límites: " + coordinate);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return totalShipHealth <= 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Debe limpiar el tablero de forma óptima según la estrategia que se
     *           use.
     */
    @Override
    public final void clear() {
        // Limpia el mapa manteniendo su capacidad en memoria
        Arrays.fill(shipsGrid, ShipType.NONE);

        for (int i = 0; i < ShipType.COUNT; i++) {
            shipHealths[i] = ShipType.LENGTHS[i];
        }
        this.totalShipHealth = ShipType.TOTAL_HEALTHS;

        clearBoardGrid();
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Según el tipo de tablero utilizado debe disparar a la celda
     *           correspondiente
     */
    @Override
    public final void shoot(int coord) {
        validateCoordinates(coord);

        byte state = getCellState(coord);

        if (state == CellState.SHIP) {
            setCellState(coord, CellState.HIT);
            byte affectedShip = shipsGrid[coord];
            if (affectedShip != ShipType.NONE) {
                shipHealths[affectedShip]--;
                totalShipHealth--;
            }
        } else if (state == CellState.WATER) {
            setCellState(coord, CellState.MISS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void putShip(int coordinate, byte ship) {
        shipsGrid[coordinate] = ship;
    }

    /**
     * Restablece el grid del tablero a su estado inicial
     */
    protected abstract void clearBoardGrid();

    /**
     * {@inheritDoc}
     */
    public abstract byte getCellState(int coordinate);

    /**
     * {@inheritDoc}
     */
    public abstract void setCellState(int coordinate, byte state);
}
