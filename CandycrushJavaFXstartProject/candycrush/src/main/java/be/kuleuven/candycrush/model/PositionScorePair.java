package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record PositionScorePair(ArrayList<PositionPairs> pairs, Integer score) {
    public PositionScorePair changeScore(Integer newScore) {
        return new PositionScorePair(this.pairs, newScore);
    }
}