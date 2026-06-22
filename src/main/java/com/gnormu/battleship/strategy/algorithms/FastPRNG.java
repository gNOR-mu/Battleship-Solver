package com.gnormu.battleship.strategy.algorithms;

/**
 * Generador de números pseudoaleatorios (PRNG) ultrarrápido y no seguro para
 * hilos.
 * Utiliza el algoritmo Xorshift64 con mapeo por multiplicación directa para
 * evitar el uso costoso del operador de división/módulo (%).
 */
public final class FastPRNG {
    private long state;

    public FastPRNG() {
        // Semilla basada en nanoTime e ID de hilo para asegurar unicidad por hilo
        long s = System.nanoTime() ^ Thread.currentThread().threadId();
        this.state = s == 0 ? 1 : s;
    }

    public FastPRNG(long seed) {
        this.state = seed == 0 ? 1 : seed;
    }

    /**
     * Genera un entero pseudoaleatorio en el rango [0, bound).
     * Utiliza mapeo multiplicativo rápido en O(1).
     * 
     * @param bound Límite superior exclusivo.
     * @return Entero pseudoaleatorio.
     */
    public int nextInt(int bound) {
        // Xorshift64
        state ^= (state << 13);
        state ^= (state >>> 7);
        state ^= (state << 17);

        // Mapea el valor de 32 bits sin signo al rango [0, bound) sin divisiones
        long r = state & 0xFFFFFFFFL;
        return (int) ((r * bound) >>> 32);
    }

    /**
     * Genera un valor booleano pseudoaleatorio.
     * 
     * @return Booleano pseudoaleatorio.
     */
    public boolean nextBoolean() {
        // Xorshift64
        state ^= (state << 13);
        state ^= (state >>> 7);
        state ^= (state << 17);
        return (state & 1) == 0;
    }
}
