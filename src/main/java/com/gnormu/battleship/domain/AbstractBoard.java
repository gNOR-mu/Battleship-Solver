package com.gnormu.battleship.domain;

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

    /**
     * Arreglo primitivo para la vida de los barcos (patrón Flyweight), si bien es
     * posible eliminarlo para saber si un juego se ha terminado, por conveniencia
     * permite mantenerlo para saber si un determinado barco sigue en juego (vida
     * > 0)
     */
    protected final byte[] shipHealths = new byte[CellContent.SHIP_SIZE];

    /** Vida total de todos los barcos colocados en el tablero */
    protected int remainingHealths = CellContent.TOTAL_LIFES;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        return remainingHealths <= 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Debe limpiar el tablero de forma óptima según la estrategia que se
     *           use.
     */
    @Override
    public final void reset() {
        CellContent.copyLength(shipHealths);
        remainingHealths = CellContent.TOTAL_LIFES;

        clearBoardGrid();
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Según el tipo de tablero utilizado debe disparar a la celda
     *           correspondiente
     * 
     * @throws IndexOutOfBoundsException Cuando la coordenada se encuentra fuera de
     *                                   los límites del tablero
     */
    @Override
    public final byte shoot(byte coord) {
        byte originalState = getCellState(coord);

        // si el estado original es mayor a 0 significa que hay un barco
        if (originalState > 0) {
            // invierto su valor
            setCellState(coord, (byte) -originalState);
            shipHealths[originalState]--;
            remainingHealths--;

        } else if (originalState == 0) {
            setCellState(coord, CellContent.MISS);
        }

        return originalState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isShipSunk(byte ship) {
        return shipHealths[ship] == 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Utiliza el nombre de la clase
     */
    @Override
    public String getBoardName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Restablece el grid del tablero a su estado inicial
     */
    protected abstract void clearBoardGrid();

}
