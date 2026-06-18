package com.gnormu.battleship.strategy.algorithms;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.gnormu.battleship.config.GameConfig;

/**
 * Algoritmo de Fisher-Yates Shuffle para seleccionar una celda aleatoria de
 * las que quedan por disparar.
 * <p>
 * Consiste en seleccionar un índice aleatorio entre los celdas no visitadas y
 * eliminarlo de la lista de celdas no visitadas.
 */
public class FisherYatesShuffle {

    /** Dimensión del arreglo a utilizar */
    private static final int DIMENSION = GameConfig.DIMENSION_SQUARED;

    /** Tablero de solo lectura para optimizar el rendimiento al limpiar */
    private static final int[] READ_GRID = generateSequence();

    /**
     * Genera una secuencia numérica [0, {@link FisherYatesShuffle#DIMENSION})
     * 
     * @return
     */
    private static int[] generateSequence() {
        return IntStream.range(0, DIMENSION).toArray();
    }

    /** Secuencia de números a utilizar */
    private final int[] sequence;

    /** Mapeo inverso de celda a su índice actual en el arreglo sequence */
    private final int[] cellToIndex;

    /** Cantidad de opciones disponibles */
    private int remainingCells;

    /** Constructor de la clase */
    public FisherYatesShuffle() {
        sequence = new int[DIMENSION];
        cellToIndex = new int[DIMENSION];
        reset();
    }

    /**
     * Extrae un elemento aleatorio de la secuencia de opciones disponibles y lo
     * elimina en O(1)
     * 
     * @return Posición lineal correspondiente a la coordenada
     */
    public int drawRandom() {
        if (remainingCells == 0) {
            return -1;
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(remainingCells);
        int cell = sequence[randomIndex];
        removeAtIndex(randomIndex);
        return cell;
    }

    /**
     * Vuelve la clase a su estado original
     */
    public void reset() {
        System.arraycopy(READ_GRID, 0, sequence, 0, DIMENSION);
        System.arraycopy(READ_GRID, 0, cellToIndex, 0, DIMENSION);
        remainingCells = DIMENSION;
    }

    /**
     * Descarta un valor de la secuencia para futuras elecciones en tiempo O(1)
     * usando búsqueda inversa.
     * 
     * @param value El valor de la celda a descartar
     * @return true si se removió exitosamente, false si ya había sido removida
     */
    public boolean remove(int value) {
        int pos = cellToIndex[value];
        if (pos >= 0 && pos < remainingCells && sequence[pos] == value) {
            removeAtIndex(pos);
            return true;
        }
        return false;
    }

    /**
     * Descarta una elección de la secuencia para futuras elecciones intercambiando
     * su posición con la última celda sin utilizar
     * 
     * @param pos Posición del elemento a descartar
     */
    private void removeAtIndex(int pos) {
        remainingCells--;
        int lastCell = sequence[remainingCells];
        sequence[pos] = lastCell;
        cellToIndex[lastCell] = pos;
    }
}
