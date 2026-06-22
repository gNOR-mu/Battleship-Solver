package com.gnormu.battleship.domain;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;

public class RandomFleetPlacer implements FleetPlacer {

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
        ThreadLocalRandom random = ThreadLocalRandom.current();
        byte length = CellContent.getLength(ship);
        byte dimension = GameConfig.BOARD_DIMENSION;
        int maxStart = dimension - length;

        while (true) {
            boolean horizontal = random.nextBoolean();
            int row, col;
            byte baseCoord;
            int step;

            if (horizontal) {
                row = random.nextInt(dimension);
                col = random.nextInt(maxStart + 1);
                baseCoord = (byte) (row * dimension + col);
                step = 1;
            } else {
                row = random.nextInt(maxStart + 1);
                col = random.nextInt(dimension);
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
