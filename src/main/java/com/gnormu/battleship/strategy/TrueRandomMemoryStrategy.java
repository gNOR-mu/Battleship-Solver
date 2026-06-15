package com.gnormu.battleship.strategy;

import java.util.concurrent.ThreadLocalRandom;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.BoardView;

/**
 * Algoritmo de resolución ineficiente que dispara aleatoriamente sin considerar
 * si atina o falla. Simplemente dispara a coordenadas al azar.
 * 
 * Utiliza una implementación del algoritmo Fisher-Yates Shuffle para
 * seleccionar una celda aleatoria de las que quedan por disparar.
 * 
 */
public class TrueRandomMemoryStrategy implements BattleshipStrategy {

    private static final String STRATEGY_NAME = "TrueRandom - Con memoria";
    private static final int[] EMPTY_CELLS = new int[GameConfig.DIMENSION_SQUARED];

    private final int[] emptyCells;
    private int remainingCells;

    static {
        // inicializa una única vez la celdas, luego se aplica un copy en el constructor
        for (int i = 0; i < GameConfig.DIMENSION_SQUARED; i++) {
            EMPTY_CELLS[i] = i;
        }
    }

    public TrueRandomMemoryStrategy() {
        remainingCells = GameConfig.DIMENSION_SQUARED;
        emptyCells = new int[GameConfig.DIMENSION_SQUARED];
        System.arraycopy(EMPTY_CELLS, 0, this.emptyCells, 0, EMPTY_CELLS.length);
    }

    /**
     * {@inheritDoc}
     * 
     * @implNote Utiliza una variación del algoritmo de Fisher-Yates Shuffle para
     *           seleccionar una celda aleatoria. Consiste en seleccionar un índice
     *           aleatorio entre los celdas no visitadas y eliminarlo de la lista
     *           de celdas no visitadas.
     * 
     */
    @Override
    public int calculateNextShot(BoardView boardView) {
        // celda al azar no disparada
        int randomIndex = ThreadLocalRandom.current().nextInt(remainingCells);

        // obtiene la celda correspondiente
        int cell = emptyCells[randomIndex];

        // decrementa el número de celdas restantes
        remainingCells--;

        // mueve la última celda no visitada a la posición de la celda seleccionada
        emptyCells[randomIndex] = emptyCells[remainingCells];

        return cell;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStrategyName() {
        return STRATEGY_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        System.arraycopy(EMPTY_CELLS, 0, this.emptyCells, 0, EMPTY_CELLS.length);
        remainingCells = GameConfig.DIMENSION_SQUARED;
    }

}
