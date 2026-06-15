package com.gnormu.battleship.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.FleetPlacer;
import com.gnormu.battleship.domain.ShipType;
import com.gnormu.battleship.strategy.BattleshipStrategy;

/**
 * Analizador de métricas que resuelve tableros usando paralelismo
 */
public class MetricAnalyzer {

    private final AtomicInteger totalTurns = new AtomicInteger(0);
    private final AtomicInteger perfectGames = new AtomicInteger(0);
    private final AtomicInteger worstGameTurns = new AtomicInteger(0);
    private final AtomicInteger bestGameTurns = new AtomicInteger(Integer.MAX_VALUE);
    private int lastTotalGames = 0;

    /**
     * Ejecuta la simulación de juegos utilizando hilos
     * 
     * @param config     Configuración de la simulación
     * @param totalGames Número de juegos a resolver
     */
    public void runSimulations(SimulationConfig config, int totalGames) {
        // limpieza de valores
        totalTurns.set(0);
        perfectGames.set(0);
        worstGameTurns.set(0);
        bestGameTurns.set(Integer.MAX_VALUE);
        lastTotalGames = totalGames;

        // numero de hilos del procesador disponibles
        int availableCores = Runtime.getRuntime().availableProcessors();
        int baseGamesPerThread = totalGames / availableCores;
        int remainder = totalGames % availableCores;

        try (ExecutorService executor = Executors.newFixedThreadPool(availableCores)) {
            // trabajo que va a realizar cada hilo
            for (int i = 0; i < availableCores; i++) {
                // asigna el resto de hilos
                final int gamesCount = baseGamesPerThread + (i < remainder ? 1 : 0);
                if (gamesCount == 0) {
                    continue;
                }
                BattleshipStrategy strategy = config.strategyFactory().get();

                executor.submit(() -> {
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
                        localBoard.clear();
                        placer.placeShips(localBoard);
                        // establece la estrategia a su estado inicial
                        strategy.reset();

                        int turns = engine.resolve(strategy);
                        localTurns += turns;

                        if (turns == ShipType.TOTAL_HEALTHS) {
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
                    totalTurns.addAndGet(localTurns);
                    perfectGames.addAndGet(localPerfect);
                    worstGameTurns.accumulateAndGet(localMax, Math::max);
                    bestGameTurns.accumulateAndGet(localMin, Math::min);
                });
            }
            executor.shutdown();
            // tiempo máximo antes de terminar
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la cantidad de turnos totales
     * 
     * @return Cantidad de turnos totales
     */
    public double getAverageTurns() {
        return (double) totalTurns.get() / lastTotalGames;
    }

    /**
     * Obtiene la cantidad de juegos perfectos (resueltos en el mínimo de turnos)
     * 
     * @return Cantidad de juegos perfectos
     */
    public int getPerfectGames() {
        return perfectGames.get();
    }

    /**
     * Obtiene la cantidad máxima de turnos que tomó resolver un juego (peor juego)
     * 
     * @return Turnos del peor juego
     */
    public int getWorstGameTurns() {
        return worstGameTurns.get();
    }

    /**
     * Obtiene la cantidad mínima de turnos que tomó resolver un juego (mejor juego)
     * 
     * @return Turnos del mejor juego
     */
    public int getBestGameTurns() {
        int best = bestGameTurns.get();
        return best == Integer.MAX_VALUE ? 0 : best;
    }
}
