package com.harrys.hyppo.source.api;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

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


    /**
     * @return This method returns the same thing as {@link PersistingSemantics#name()} and is only defined so that the
     * {@link JsonValue} annotation can be attached to it.
     */
    @JsonValue
    public final String getJsonName(){
        return this.name();
    }

    @JsonCreator
    public static final PersistingSemantics fromString(final String name) {
        for (final PersistingSemantics check : PersistingSemantics.values()){
            if (check.name().equalsIgnoreCase(name)){
                return check;
            }
        }
        throw new IllegalArgumentException("Unknown " + PersistingSemantics.class.getName() + " type: " + name);
    }
}
