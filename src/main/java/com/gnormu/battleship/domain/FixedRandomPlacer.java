package com.gnormu.battleship.domain;

import com.gnormu.battleship.config.GameConfig;

/**
 * Posicionador de flota que genera una única distribución válida de barcos
 * de forma aleatoria y estática durante su inicialización, y luego la copia
 * en cada tablero.
 * 
 * @implNote Usado como referencia de un posicionador con tiempos casi perfectos
 *           utilizando {@link RandomFleetPlacer} una única vez
 */
public class FixedRandomPlacer implements FleetPlacer {

    private static final byte[] SHIP_COORDS = new byte[CellContent.TOTAL_LIFES];
    private static final byte[] SHIP_IDS = new byte[CellContent.TOTAL_LIFES];

    static {
        Board1D tempBoard = new Board1D();
        new RandomFleetPlacer().placeShips(tempBoard);

        int count = 0;
        for (byte i = 0; i < GameConfig.DIMENSION_SQUARED; i++) {
            byte ship = tempBoard.getCellState(i);
            if (ship > 0) {
                SHIP_COORDS[count] = i;
                SHIP_IDS[count] = ship;
                count++;
            }
        }
    }

    @Override
    public void placeShips(Board board) {
        for (int i = 0; i < CellContent.TOTAL_LIFES; i++) {
            board.putShip(SHIP_COORDS[i], SHIP_IDS[i]);
        }
    }
}
