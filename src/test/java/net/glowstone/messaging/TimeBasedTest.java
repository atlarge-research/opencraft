package net.glowstone.messaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * A time-based test is a test that uses a timeout to determine whether a test succeeded or failed. While timeouts have
 * been chosen with care, the final results remain dependent on the running system. It may therefore be wise to exclude
 * these tests from runs in which there is no manual supervision of or extensive control over the system on which the
 * tests are ran.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Tag("time-based")
@Test
public @interface TimeBasedTest {}
