package science.atlarge.opencraft.opencraft.net;

import com.flowpowered.network.Message;
import science.atlarge.opencraft.opencraft.net.message.status.StatusPingMessage;
import science.atlarge.opencraft.opencraft.net.message.status.StatusRequestMessage;
import science.atlarge.opencraft.opencraft.net.message.status.StatusResponseMessage;
import science.atlarge.opencraft.opencraft.net.protocol.StatusProtocol;

/**
 * Test cases for {@link StatusProtocol}.
 */
public class StatusProtocolTest extends BaseProtocolTest {

    private static final Message[] TEST_MESSAGES = new Message[]{
        new StatusPingMessage(1),
        new StatusResponseMessage(ProtocolTestUtils.getJson()),
        new StatusRequestMessage(),
    };

    public StatusProtocolTest() {
        super(new StatusProtocol(), TEST_MESSAGES);
    }
}
