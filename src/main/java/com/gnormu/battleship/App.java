package com.gnormu.battleship;

import com.gnormu.battleship.engine.MetricEvaluator;

public class App {
    public static void main(String[] args) {
        MetricEvaluator evaluator = new MetricEvaluator();
        evaluator.evaluateAll();
    }
}
