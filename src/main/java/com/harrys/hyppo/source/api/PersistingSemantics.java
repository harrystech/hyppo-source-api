package com.harrys.hyppo.source.api;

/**
 * Created by jpetty on 10/20/15.
 */
public enum PersistingSemantics {
    /**
     * Persisting tasks are assumed not to be retryable by default. When a persitence task fails or is interrupted, it
     * can't be safely retried automatically unless it declares {@link PersistingSemantics#Idempotent}.
     */
    Default,

    /**
     * Implies that the task may be executed either partially or completely multiple times without negative effects. This
     * is in contrast to {@link PersistingSemantics#Default} where the task might not be safe to run multiple times and it is
     * therefore not appropriate to respond to a failure by retrying.
     */
    Idempotent;
}
