package net.glowstone.net.message.login;

import com.flowpowered.networking.AsyncableMessage;
import lombok.Data;

@Data
public final class LoginStartMessage implements AsyncableMessage {

    private final String username;

}
