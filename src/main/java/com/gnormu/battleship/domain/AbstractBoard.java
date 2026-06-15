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
    protected final ShipType[] shipsGrid;

    /** Vida total de todos los barcos colocados en el tablero */
    protected int totalShipHealth;

    public AbstractBoard() {
        this.shipHealths = new int[ShipType.VALUES.size()];
        this.shipsGrid = new ShipType[GameConfig.BOARD_DIMENSION * GameConfig.BOARD_DIMENSION];
    }

    /**
     * Valida que las coordenadas permanezcan estrictamente dentro del tablero
     * 
     * @param row Fila a validar
     * @param col Columna a validar
     * 
     * @throws IllegalArgumentException Cuando la fila (row) o columna (col) se
     *                                  encuentran fuera de los límites del tablero
     */
    protected void validateCoordinates(int row, int col) {
        if (row < 0 || row >= GameConfig.BOARD_DIMENSION
                || col < 0 || col >= GameConfig.BOARD_DIMENSION) {
            throw new IllegalArgumentException(
                    "Disparo fuera de los límites: " + row + "," + col);
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
        Arrays.fill(shipsGrid, null);

        System.arraycopy(ShipType.INITIAL_HEALTHS, 0, shipHealths, 0, ShipType.INITIAL_HEALTHS.length);
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
    public final void shoot(Coordinate coord) {
        int row = coord.row();
        int col = coord.column();

        validateCoordinates(row, col);

        CellState state = getCellState(row, col);

        if (state == CellState.SHIP) {
            setCellState(row, col, CellState.HIT);
            ShipType affectedShip = shipsGrid[row * GameConfig.BOARD_DIMENSION + col];
            if (affectedShip != null) {
                shipHealths[affectedShip.ordinal()]--;
                totalShipHealth--;
            }
        } else if (state == CellState.WATER) {
            setCellState(row, col, CellState.MISS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void putShip(Coordinate coordinate, ShipType ship) {
        shipsGrid[coordinate.row() * GameConfig.BOARD_DIMENSION + coordinate.column()] = ship;
    }

    /**
     * Restablece el grid del tablero a su estado inicial
     */
    protected abstract void clearBoardGrid();

    /**
     * {@inheritDoc}
     */
    public abstract CellState getCellState(int row, int col);

    /**
     * {@inheritDoc}
     */
    public abstract void setCellState(int row, int col, CellState state);
}
