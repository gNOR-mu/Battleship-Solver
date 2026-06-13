package com.gnormu.battleship.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import com.gnormu.battleship.engine.MetricAnalyzer;
import com.gnormu.battleship.strategy.BattleshipStrategy;
import com.gnormu.battleship.strategy.TrueRandomStrategy;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SolverBenchmark {

    private MetricAnalyzer metricAnalyzer;

    @Param({ "500000" })
    public int totalGames;

    @Setup(Level.Trial)
    public void setup() {
        metricAnalyzer = new MetricAnalyzer();
    }

    @Benchmark
    public double testMetricAnalyzerWithTrueRandom() {
        BattleshipStrategy strategy = new TrueRandomStrategy();

        metricAnalyzer.runSimulations(strategy, totalGames);
        return metricAnalyzer.getAverageTurns();
    }
}
