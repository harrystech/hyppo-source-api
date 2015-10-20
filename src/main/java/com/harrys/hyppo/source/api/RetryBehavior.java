package com.harrys.hyppo.source.api;

/**
 * Created by jpetty on 10/20/15.
 */
public enum RetryBehavior {
    /**
     * Default RetryBehavior varies depending on the work being performed. In almost all cases, a task is assumed to
     * retryable <b>except</b> when persisting. Under those cases, a task is not retryable unless it promises to be
     * {@link RetryBehavior#Idempotent}.
     */
    Default,

    /**
     * Implies that the task may be executed either partially or completely multiple times without negative effects. This
     * is in contrast to {@link RetryBehavior#Default} where the task might not be safe to run multiple times and it is
     * therefore not appropriate to respond to a failure by retrying.
     */
    Idempotent;
}
