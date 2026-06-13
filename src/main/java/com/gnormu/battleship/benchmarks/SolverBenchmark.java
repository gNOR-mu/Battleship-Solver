package com.gnormu.battleship.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

import com.gnormu.battleship.engine.MetricAnalyzer;
import com.gnormu.battleship.strategy.BruteForceStrategy;
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
    public double trueRandomStrategy() {
        metricAnalyzer.runSimulations(TrueRandomStrategy::new, totalGames);
        return metricAnalyzer.getAverageTurns();
    }

    @Benchmark
    public double bruteForceStrategy() {
        metricAnalyzer.runSimulations(BruteForceStrategy::new, totalGames);
        return metricAnalyzer.getAverageTurns();
    }
}
