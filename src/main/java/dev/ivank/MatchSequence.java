package dev.ivank;

import java.util.concurrent.atomic.AtomicLong;

public final class MatchSequence {
    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private MatchSequence() {
    }

    public static long nextOrder() {
        return SEQUENCE.incrementAndGet();
    }
}
