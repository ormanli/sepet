package com.serdarormanli.sepet.iotsimulator;

import java.util.PrimitiveIterator;
import java.util.stream.DoubleStream;

public class TemperatureGenerator implements PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;

    TemperatureGenerator(int atLeast) {
        iterator = DoubleStream.iterate(0, operand -> operand + 0.05)
                .map(Math::sin)
                .map(operand -> operand + 2)
                .map(operand -> operand * atLeast)
                .iterator();
    }

    @Override
    public double nextDouble() {
        return iterator.nextDouble();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
