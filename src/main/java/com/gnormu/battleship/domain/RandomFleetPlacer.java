package com.gnormu.battleship.domain;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.strategy.algorithms.FastPRNG;

public class RandomFleetPlacer implements FleetPlacer {

    private final FastPRNG prng = new FastPRNG();

    /**
     * {@inheritDoc}
     * 
     * @implNote Posiciona todos los barcos en el tablero de forma aleatoria
     */
    @Override
    public void placeShips(Board board) {
        for (byte ship = 0; ship < CellContent.SHIP_SIZE; ship++) {
            if (CellContent.getLength(ship) > 0) {
                placeShip(board, ship);
            }
        }
    }

    /**
     * Posiciona un barco en el tablero, puede estar posicionado de forma vertical u
     * horizontal
     * 
     * @param ship Barco a posicionar
     */
    private void placeShip(Board board, byte ship) {
        byte length = CellContent.getLength(ship);
        byte dimension = GameConfig.BOARD_DIMENSION;
        int maxStart = dimension - length;

        while (true) {
            boolean horizontal = prng.nextBoolean();
            int row, col;
            byte baseCoord;
            int step;

            if (horizontal) {
                row = prng.nextInt(dimension);
                col = prng.nextInt(maxStart + 1);
                baseCoord = (byte) (row * dimension + col);
                step = 1;
            } else {
                row = prng.nextInt(maxStart + 1);
                col = prng.nextInt(dimension);
                baseCoord = (byte) (row * dimension + col);
                step = dimension;
            }

            // Verificación de ocupación (si cabe)
            boolean fits = true;
            for (byte i = 0; i < length; i++) {
                if (board.getCellState((byte) (baseCoord + i * step)) != CellContent.WATER) {
                    fits = false;
                    break;
                }
            }

            // Posicionamiento
            if (fits) {
                for (byte i = 0; i < length; i++) {
                    byte coord = (byte) (baseCoord + i * step);
                    board.putShip(coord, ship);
                }
                break;
            }
        }
    }

}
