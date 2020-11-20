package science.atlarge.opencraft.opencraft.net;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * A list of all the sessions which provides a convenient {@link #pulse()} method to pulse every
 * session in one operation.
 *
 * @author Graham Edgecombe
 */
public final class SessionRegistry {

    private final LongAdder adder = new LongAdder();

    /**
     * A list of the sessions.
     */
    private final ConcurrentMap<GlowSession, Boolean> sessions = new ConcurrentHashMap<>();

    /**
     * Pulses all the sessions.
     */
    public void pulse() {
        sessions.keySet().forEach(GlowSession::pulse);
    }

    /**
     * Adds a new session.
     *
     * @param session The session to add.
     */
    public void add(GlowSession session) {
        session.setPacketReceivedCounter(adder);
        sessions.put(session, true);
    }

    /**
     * Removes a session.
     *
     * @param session The session to remove.
     */
    public void remove(GlowSession session) {
        sessions.remove(session);
    }

    public long totalReceivedMessages() {
        return adder.sum();
    }

}
