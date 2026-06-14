package com.gnormu.battleship.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.gnormu.battleship.domain.Board2d;
import com.gnormu.battleship.domain.FleetPlacer;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.strategy.BattleshipStrategy;

/**
 * Analizador de métricas que resuelve tableros usando paralelismo
 */
public class MetricAnalyzer {

    private final AtomicInteger totalTurns = new AtomicInteger(0);
    private int lastTotalGames = 0;

    /**
     * Ejecuta la simulación de juegos utilizando hilos
     * 
     * @param strategy   Estrategia a utilizar
     * @param totalGames Número de juegos a resolver
     */
    public void runSimulations(SimulationConfig config, int totalGames) {
        // limpieza de valores
        totalTurns.set(0);
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
                    // todo reemplazar
                    Board2d localBoard = new Board2d();
                    FleetPlacer placer = new RandomFleetPlacer();
                    GameEngine engine = new GameEngine(config);
                    int localTurns = 0;

                    // juegos que resuelve cada hilo
                    for (int j = 0; j < gamesCount; j++) {
                        // inicializa el tablero
                        localBoard.clear();
                        placer.placeShips(localBoard);
                        // establece la estrategia a su estado inicial
                        strategy.reset();

                        localTurns += engine.resolve(strategy);
                    }
                    // añado a la metrica total la cantidad de turnos del hilo
                    totalTurns.addAndGet(localTurns);
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
}
