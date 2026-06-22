package com.gnormu.battleship.strategy;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;
import com.gnormu.battleship.domain.CellContent;
import com.gnormu.battleship.strategy.algorithms.FisherYatesShuffle;

/**
 * Estrategia de resolución de Hunt and Target optimizada usando la información
 * de los tipos de barcos al impactar.
 */
public class HuntTargetStrategy extends AbstractBattleshipStrategy {

    private final FisherYatesShuffle shuffler;

    private final byte[][] shipHits;
    private final byte[] shipHitCounts;

    private byte lastCoord;

    public HuntTargetStrategy() {
        this.shuffler = new FisherYatesShuffle();
        this.shipHits = new byte[CellContent.SHIP_SIZE][CellContent.MAX_SHIP_SIZE];
        this.shipHitCounts = new byte[CellContent.SHIP_SIZE];

        reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte calculateNextShot(BoardView boardView) {
        // Registrar impacto si hubo un disparo anterior
        if (lastCoord != -1) {
            byte lastShotState = boardView.getCellState(lastCoord);
            if (lastShotState < 0 && lastShotState != CellContent.MISS) {
                byte ship = (byte) -lastShotState;
                shipHits[ship][shipHitCounts[ship]++] = lastCoord;
            }
        }

        byte nextShot = -1;

        // Buscar si hay algún barco activo (tocado pero no hundido)
        for (byte ship = 1; ship < CellContent.SHIP_SIZE; ship++) {
            if (shipHitCounts[ship] > 0 && !boardView.isShipSunk(ship)) {
                nextShot = getNextTargetForShip(boardView, ship);
                if (nextShot != -1) {
                    break;
                }
            }
        }

        // Si no hay barcos activos o todos sus objetivos están bloqueados, disparamos
        // al azar
        if (nextShot == -1) {
            nextShot = shuffler.drawRandom();
        } else {
            shuffler.remove(nextShot);
        }

        lastCoord = nextShot;
        return nextShot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        shuffler.reset();
        this.lastCoord = -1;
        for (int i = 0; i < CellContent.SHIP_SIZE; i++) {
            shipHitCounts[i] = 0;
        }
    }

    /**
     * Calcula la coordenada del siguiente disparo para un barco determinado.
     * <p>
     * Si el barco ha sido impactado una sola vez, se buscan celdas adyacentes
     * (arriba, abajo, izquierda, derecha) que aún no han sido disparadas.
     * Si ha sido impactado dos o más veces, se determina la orientación
     * (horizontal o vertical) y se prioriza disparar a los extremos de la línea
     * de impactos.
     * 
     * @param boardView Tablero de solo lectura con información pública del juego
     * @param ship      Identificador del barco que está siendo cazado
     * @return Coordenada lineal a disparar, o -1 si no hay objetivos válidos para
     *         este barco
     */
    private byte getNextTargetForShip(BoardView boardView, byte ship) {
        int count = shipHitCounts[ship];
        if (count == 0) {
            return -1;
        }

        byte dim = GameConfig.BOARD_DIMENSION;

        if (count == 1) {
            byte h = shipHits[ship][0];
            byte neighbor;

            // Arriba
            if (h >= dim) {
                neighbor = (byte) (h - dim);
                if (boardView.getCellState(neighbor) == CellContent.WATER) {
                    return neighbor;
                }
            }
            // Abajo
            if (h < dim * (dim - 1)) {
                neighbor = (byte) (h + dim);
                if (boardView.getCellState(neighbor) == CellContent.WATER) {
                    return neighbor;
                }
            }
            // Izquierda
            if (h % dim > 0) {
                neighbor = (byte) (h - 1);
                if (boardView.getCellState(neighbor) == CellContent.WATER) {
                    return neighbor;
                }
            }
            // Derecha
            if (h % dim < dim - 1) {
                neighbor = (byte) (h + 1);
                if (boardView.getCellState(neighbor) == CellContent.WATER) {
                    return neighbor;
                }
            }
        } else {
            // Determinar orientación (vertical si la diferencia es múltiplo de dim)
            boolean vertical = Math.abs(shipHits[ship][0] - shipHits[ship][1]) % dim == 0;

            byte minH = shipHits[ship][0];
            byte maxH = shipHits[ship][0];
            for (int i = 1; i < count; i++) {
                byte h = shipHits[ship][i];
                if (h < minH)
                    minH = h;
                if (h > maxH)
                    maxH = h;
            }

            if (vertical) {
                // Arriba
                if (minH >= dim) {
                    byte next = (byte) (minH - dim);
                    if (boardView.getCellState(next) == CellContent.WATER) {
                        return next;
                    }
                }
                // Abajo
                if (maxH < dim * (dim - 1)) {
                    byte next = (byte) (maxH + dim);
                    if (boardView.getCellState(next) == CellContent.WATER) {
                        return next;
                    }
                }
            } else {
                // Izquierda
                if (minH % dim > 0) {
                    byte next = (byte) (minH - 1);
                    if (boardView.getCellState(next) == CellContent.WATER) {
                        return next;
                    }
                }
                // Derecha
                if (maxH % dim < dim - 1) {
                    byte next = (byte) (maxH + 1);
                    if (boardView.getCellState(next) == CellContent.WATER) {
                        return next;
                    }
                }
            }
        }

        return -1;
    }
}