package com.gnormu.battleship.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.gnormu.battleship.config.GameConfig;
import com.gnormu.battleship.domain.Board;
import com.gnormu.battleship.domain.Board1D;
import com.gnormu.battleship.domain.RandomFleetPlacer;
import com.gnormu.battleship.strategy.BattleshipStrategy;
import com.gnormu.battleship.strategy.BruteForceStrategy;
import com.gnormu.battleship.strategy.TrueRandomMemoryStrategy;
import com.gnormu.battleship.strategy.TrueRandomStrategy;
import com.gnormu.battleship.strategy.HuntTargetStrategy;

/**
 * Clase encargada de evaluar y comparar las métricas de eficiencia
 * entre las distintas combinaciones de tableros y solvers.
 */
public class MetricEvaluator {

    private static class EvaluationResult {
        String boardName;
        String strategyName;
        double averageTurns;
        int perfectGames;
        int bestGameTurns;
        int worstGameTurns;

        public EvaluationResult(String boardName, String strategyName, double averageTurns, int perfectGames,
                int bestGameTurns, int worstGameTurns) {
            this.boardName = boardName;
            this.strategyName = strategyName;
            this.averageTurns = averageTurns;
            this.perfectGames = perfectGames;
            this.bestGameTurns = bestGameTurns;
            this.worstGameTurns = worstGameTurns;
        }
    }

    /**
     * Ejecuta la evaluación completa para todas las combinaciones y muestra el
     * reporte en consola.
     */
    public void evaluateAll() {
        int totalGames = 500_000; // Cantidad de partidas a simular por cada combinación

        String mainHeaderBorder = generateSeparator('=', 80);

        System.out.println(mainHeaderBorder);
        System.out.println("                    BATTLESHIP MULTISOLVER EVALUATOR");
        System.out.println(mainHeaderBorder);
        System.out.println("Partidas por combinación: " + String.format("%,d", totalGames));
        System.out.println("Dimensión del Tablero: " + GameConfig.BOARD_DIMENSION + "x" + GameConfig.BOARD_DIMENSION);
        System.out.println(mainHeaderBorder);

        List<EvaluationResult> results = new ArrayList<>();

        Supplier<Board> board = Board1D::new;

        // Combinaciones de Estrategias
        List<Supplier<BattleshipStrategy>> strategyFactories = List.of(
                BruteForceStrategy::new,
                TrueRandomStrategy::new,
                TrueRandomMemoryStrategy::new,
                HuntTargetStrategy::new);

        MetricAnalyzer analyzer = new MetricAnalyzer();

        for (Supplier<BattleshipStrategy> strategyFactory : strategyFactories) {
            SimulationConfig config = new SimulationConfig(strategyFactory, board, RandomFleetPlacer::new);

            // Ejecución directa de la medición sin calentamiento
            analyzer.runSimulations(config, totalGames);

            double avgTurns = analyzer.getAverageTurns();
            int perfect = analyzer.getPerfectGames();
            int best = analyzer.getBestGameTurns();
            int worst = analyzer.getWorstGameTurns();
            results.add(new EvaluationResult(
                    board.get().getBoardName(),
                    strategyFactory.get().getStrategyName(),
                    avgTurns,
                    perfect,
                    best,
                    worst));
        }

        // Definición de anchos de columna para cálculo dinámico del borde del reporte
        int[] colWidths = { 12, 32, 12, 16, 12, 12 };
        int tableWidth = colWidths.length * 3 + 1;
        for (int w : colWidths) {
            tableWidth += w;
        }

        String tableSeparator = generateSeparator('-', tableWidth);
        String tableDoubleSeparator = generateSeparator('=', tableWidth);

        // Imprimir reporte con formato de tabla
        System.out.println("\n" + tableSeparator);
        System.out.printf("| %-12s | %-32s | %-12s | %-16s | %-12s | %-12s |\n", "Tablero", "Estrategia",
                "Turnos Prom.", "Juegos Perfectos", "Mejor Juego", "Peor Juego");
        System.out.println(tableSeparator);
        for (EvaluationResult res : results) {
            System.out.printf("| %-12s | %-32s | %-12.2f | %-16d | %-12d | %-12d |\n",
                    res.boardName, res.strategyName, res.averageTurns, res.perfectGames, res.bestGameTurns,
                    res.worstGameTurns);
        }
        System.out.println(tableSeparator);
        System.out.println(tableDoubleSeparator);
    }

    /**
     * Genera una línea repetitiva de caracteres con un largo determinado.
     * 
     * @param character Carácter a repetir
     * @param length    Cantidad de veces a repetir
     * @return Línea construida
     */
    private String generateSeparator(char character, int length) {
        return String.valueOf(character).repeat(Math.max(0, length));
    }
}
