package com.gnormu.battleship.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.Board1d;
import com.gnormu.battleship.domain.Board2d;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.strategy.BattleshipStrategy;
import com.gnormu.battleship.strategy.BruteForceStrategy;
import com.gnormu.battleship.strategy.TrueRandomStrategy;

/**
 * Clase encargada de evaluar y comparar las métricas de eficiencia
 * entre las distintas combinaciones de tableros y solvers.
 */
public class MetricEvaluator {

    private static class EvaluationResult {
        String boardName;
        String strategyName;
        double averageTurns;

        public EvaluationResult(String boardName, String strategyName, double averageTurns) {
            this.boardName = boardName;
            this.strategyName = strategyName;
            this.averageTurns = averageTurns;
        }
    }

    /**
     * Ejecuta la evaluación completa para todas las combinaciones y muestra el
     * reporte en consola.
     */
    public void evaluateAll() {
        int totalGames = 500_000; // Cantidad de partidas a simular por cada combinación

        System.out.println("================================================================================");
        System.out.println("                    BATTLESHIP MULTISOLVER EVALUATOR");
        System.out.println("================================================================================");
        System.out.println("Partidas por combinación: " + String.format("%,d", totalGames));
        System.out.println("Dimensión del Tablero: " + GameConfig.BOARD_DIMENSION + "x" + GameConfig.BOARD_DIMENSION);
        System.out.println("================================================================================");

        List<EvaluationResult> results = new ArrayList<>();

        // Combinaciones de Tableros
        List<Supplier<Board>> boardFactories = List.of(
                Board1d::new,
                Board2d::new);
        String[] boardNames = { "Board1D", "Board2D" };

        // Combinaciones de Estrategias
        List<Supplier<BattleshipStrategy>> strategyFactories = List.of(
                BruteForceStrategy::new,
                TrueRandomStrategy::new);
        String[] strategyNames = { "BruteForce", "TrueRandom" };

        MetricAnalyzer analyzer = new MetricAnalyzer();

        for (int b = 0; b < boardFactories.size(); b++) {
            for (int s = 0; s < strategyFactories.size(); s++) {
                Supplier<Board> boardFactory = boardFactories.get(b);
                Supplier<BattleshipStrategy> strategyFactory = strategyFactories.get(s);

                SimulationConfig config = new SimulationConfig(strategyFactory, boardFactory, RandomFleetPlacer::new);

                // Ejecución directa de la medición sin calentamiento
                analyzer.runSimulations(config, totalGames);

                double avgTurns = analyzer.getAverageTurns();
                results.add(new EvaluationResult(boardNames[b], strategyNames[s], avgTurns));
            }
        }

        // Imprimir reporte con formato de tabla
        System.out.println("\n------------------------------------------------");
        System.out.printf("| %-12s | %-14s | %-12s |\n", "Tablero", "Estrategia", "Turnos Prom.");
        System.out.println("------------------------------------------------");
        for (EvaluationResult res : results) {
            System.out.printf("| %-12s | %-14s | %-12.2f |\n", res.boardName, res.strategyName, res.averageTurns);
        }
        System.out.println("------------------------------------------------");
        System.out.println("================================================================================");
    }
}
