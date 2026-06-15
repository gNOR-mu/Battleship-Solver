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

        Supplier<Board> board = Board1d::new;

        // Combinaciones de Estrategias
        List<Supplier<BattleshipStrategy>> strategyFactories = List.of(
                BruteForceStrategy::new,
                TrueRandomStrategy::new);

        MetricAnalyzer analyzer = new MetricAnalyzer();

        for (Supplier<BattleshipStrategy> strategyFactory : strategyFactories) {
            SimulationConfig config = new SimulationConfig(strategyFactory, board, RandomFleetPlacer::new);

            // Ejecución directa de la medición sin calentamiento
            analyzer.runSimulations(config, totalGames);

            double avgTurns = analyzer.getAverageTurns();
            results.add(new EvaluationResult(
                    board.get().getBoardName(),
                    strategyFactory.get().getStrategyName(),
                    avgTurns));
        }

        // Imprimir reporte con formato de tabla
        System.out.println("\n--------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-40s | %-12s |\n", "Tablero", "Estrategia", "Turnos Prom.");
        System.out.println("--------------------------------------------------------------------------");
        for (EvaluationResult res : results) {
            System.out.printf("| %-12s | %-40s | %-12.2f |\n", res.boardName, res.strategyName, res.averageTurns);
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("===============================================================================");
    }
}
