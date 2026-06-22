package com.gnormu.battleship.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.CellContent;
import com.gnormu.battleship.domain.FleetPlacer;
import com.gnormu.battleship.strategy.BattleshipStrategy;

/**
 * Analizador de métricas que resuelve tableros usando paralelismo
 */
public class MetricAnalyzer implements AutoCloseable {

    private final LongAdder totalTurns = new LongAdder();
    private final LongAdder perfectGames = new LongAdder();
    private final LongAccumulator worstGameTurns = new LongAccumulator(Math::max, 0);
    private final LongAccumulator bestGameTurns = new LongAccumulator(Math::min, Integer.MAX_VALUE);
    private int lastTotalGames = 0;

    private final ExecutorService executor;
    private final int availableCores;

    public MetricAnalyzer() {
        this.availableCores = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(availableCores);
    }

    /**
     * Ejecuta la simulación de juegos utilizando hilos
     * 
     * @param config     Configuración de la simulación
     * @param totalGames Número de juegos a resolver
     */
    public void runSimulations(SimulationConfig config, int totalGames) {
        // limpieza de valores
        totalTurns.reset();
        perfectGames.reset();
        worstGameTurns.reset();
        bestGameTurns.reset();
        lastTotalGames = totalGames;

        // numero de hilos del procesador disponibles
        int baseGamesPerThread = totalGames / availableCores;
        int remainder = totalGames % availableCores;

        List<Future<?>> futures = new ArrayList<>(availableCores);

        // trabajo que va a realizar cada hilo
        for (int i = 0; i < availableCores; i++) {
            // asigna el resto de hilos
            final int gamesCount = baseGamesPerThread + (i < remainder ? 1 : 0);
            if (gamesCount == 0) {
                continue;
            }
            BattleshipStrategy strategy = config.strategyFactory().get();

            futures.add(executor.submit(() -> {
                Board localBoard = config.boardFactory().get();
                FleetPlacer placer = config.placerFactory().get();
                GameEngine engine = new GameEngine(localBoard);
                int localTurns = 0;
                int localPerfect = 0;
                int localMax = 0;
                int localMin = Integer.MAX_VALUE;

                // juegos que resuelve cada hilo
                for (int j = 0; j < gamesCount; j++) {
                    // inicializa el tablero
                    localBoard.reset();
                    placer.placeShips(localBoard);
                    // establece la estrategia a su estado inicial
                    strategy.reset();

                    int turns = engine.resolve(strategy);
                    localTurns += turns;

                    if (turns == CellContent.TOTAL_LIFES) {
                         localPerfect++;
                    }
                    if (turns > localMax) {
                         localMax = turns;
                    }
                    if (turns < localMin) {
                         localMin = turns;
                    }
                }
                // añado a la metrica total la cantidad de turnos del hilo
                totalTurns.add(localTurns);
                perfectGames.add(localPerfect);
                worstGameTurns.accumulate(localMax);
                bestGameTurns.accumulate(localMin);
            }));
        }

        // esperar a que terminen todas las tareas
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Simulación interrumpida", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Error en ejecución de simulación", e.getCause());
            }
        }
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Obtiene la cantidad de turnos totales
     * 
     * @return Cantidad de turnos totales
     */
    public double getAverageTurns() {
        return (double) totalTurns.sum() / lastTotalGames;
    }

    /**
     * Obtiene la cantidad de juegos perfectos (resueltos en el mínimo de turnos)
     * 
     * @return Cantidad de juegos perfectos
     */
    public int getPerfectGames() {
        return perfectGames.intValue();
    }

    /**
     * Obtiene la cantidad máxima de turnos que tomó resolver un juego (peor juego)
     * 
     * @return Turnos del peor juego
     */
    public int getWorstGameTurns() {
        return worstGameTurns.intValue();
    }

    /**
     * Obtiene la cantidad mínima de turnos que tomó resolver un juego (mejor juego)
     * 
     * @return Turnos del mejor juego
     */
    public int getBestGameTurns() {
        int best = bestGameTurns.intValue();
        return best == Integer.MAX_VALUE ? 0 : best;
    }
}
