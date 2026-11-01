package science.atlarge.opencraft.opencraft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsoleManagerTest {

    private static final Logger ROOT = Logger.getLogger("");

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final Handler[] originalHandlers = ROOT.getHandlers();
    private final Level originalLevel = ROOT.getLevel();
    private int flushes;
    private ByteArrayOutputStream written;
    private ConsoleManager manager;

    @Before
    public void before() {
        written = new ByteArrayOutputStream();
        flushes = 0;
        // The console handler captures System.out when ConsoleManager creates it.
        System.setOut(new PrintStream(written, false) {
            @Override
            public void flush() {
                flushes++;
                super.flush();
            }
        });
        manager = new ConsoleManager(mock(GlowServer.class));
    }

    @After
    public void after() {
        for (Handler handler : ROOT.getHandlers()) {
            ROOT.removeHandler(handler);
        }
        for (Handler handler : originalHandlers) {
            ROOT.addHandler(handler);
        }
        ROOT.setLevel(originalLevel);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    private Handler consoleHandler() {
        Handler[] handlers = ROOT.getHandlers();
        assertEquals(1, handlers.length);
        return handlers[0];
    }

    @Test
    public void testRecordsBelowHandlerLevelDoNotFlush() {
        consoleHandler().setLevel(Level.INFO);
        int before = flushes;
        for (int i = 0; i < 100; i++) {
            Logger.getLogger("test").log(Level.FINEST, "dropped");
        }
        assertEquals(before, flushes);
        assertEquals(0, written.size());
    }

    @Test
    public void testRecordsAtHandlerLevelAreWritten() {
        consoleHandler().setLevel(Level.INFO);
        Logger.getLogger("test").log(Level.INFO, "shown");
        assertTrue(written.toString().contains("shown"));
    }

    @Test
    public void testLoggerLevelFollowsLowestHandlerLevel() {
        Handler console = consoleHandler();
        console.setLevel(Level.INFO);
        manager.restrictLoggerLevel();
        assertEquals(Level.INFO, ROOT.getLevel());
        assertFalse(Logger.getLogger("test").isLoggable(Level.FINEST));

        console.setLevel(Level.FINE);
        manager.restrictLoggerLevel();
        assertEquals(Level.FINE, ROOT.getLevel());
        assertTrue(Logger.getLogger("test").isLoggable(Level.FINE));
    }
}
