package com.gnormu.battleship.strategy.algorithms;

import com.gnormu.battleship.config.GameConfig;

/**
 * Algoritmo de Fisher-Yates Shuffle para seleccionar una celda aleatoria de
 * las que quedan por disparar.
 * <p>
 * Consiste en seleccionar un índice aleatorio entre las celdas no visitadas y
 * eliminarlo de la lista de celdas no visitadas.
 */
public class FisherYatesShuffle {

    /** Dimensión del arreglo a utilizar */
    private static final int DIMENSION = GameConfig.DIMENSION_SQUARED;

    /** Tablero de solo lectura para optimizar el rendimiento al limpiar */
    private static final byte[] READ_GRID = generateSequence();

    private static byte[] generateSequence() {
        byte[] arr = new byte[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            arr[i] = (byte) i;
        }
        return arr;
    }

    /** Secuencia de números a utilizar */
    private final byte[] sequence;

    /** Mapeo inverso de celda a su índice actual en el arreglo sequence */
    private final byte[] cellToIndex;

    /** Cantidad de opciones disponibles */
    private int remainingCells;

    /** Generador de números aleatorios rápido */
    private final FastPRNG prng;

    /** Constructor de la clase */
    public FisherYatesShuffle() {
        sequence = new byte[DIMENSION];
        cellToIndex = new byte[DIMENSION];
        prng = new FastPRNG();
        reset();
    }

    /**
     * Extrae un elemento aleatorio de la secuencia de opciones disponibles y lo
     * elimina en O(1)
     * 
     * @return Posición lineal correspondiente a la coordenada
     */
    public byte drawRandom() {
        if (remainingCells == 0) {
            return -1;
        }
        int randomIndex = prng.nextInt(remainingCells);
        byte cell = sequence[randomIndex];
        removeAtIndex(randomIndex);
        return cell;
    }

    /**
     * Vuelve la clase a su estado original de forma vectorizada usando System.arraycopy.
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
        if (value < 0 || value >= DIMENSION) {
            return false;
        }
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
        byte lastCell = sequence[remainingCells];
        sequence[pos] = lastCell;
        cellToIndex[lastCell] = (byte) pos;
    }
}
