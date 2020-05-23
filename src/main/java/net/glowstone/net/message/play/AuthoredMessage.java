package net.glowstone.net.message.play;

import com.flowpowered.network.Message;

/**
 * An authored message is a message that was originally written by a specific entity.
 */
public interface AuthoredMessage extends Message {

    /**
     * Get the identifier of the entity that authored the message.
     *
     * @return the entity's identifier.
     */
    int getAuthorId();
}
