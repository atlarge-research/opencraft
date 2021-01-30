package science.atlarge.opencraft.opencraft.io.nbt;

import java.io.File;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.io.ScoreboardIoService;
import science.atlarge.opencraft.opencraft.scoreboard.GlowScoreboard;
import science.atlarge.opencraft.opencraft.scoreboard.NbtScoreboardIoReader;
import science.atlarge.opencraft.opencraft.scoreboard.NbtScoreboardIoWriter;

/**
 * An implementation of the {@link ScoreboardIoService} which reads and writes scoreboards in NBT
 * form.
 */
public final class NbtScoreboardIoService implements ScoreboardIoService {

    private static final String SCOREBOARD_SAVE_FILE = "scoreboard.dat"; // NON-NLS

    /**
     * The root directory of the scoreboard.
     */
    private final File dir;
    private final GlowServer server;

    public NbtScoreboardIoService(GlowServer server, File dir) {
        this.server = server;
        this.dir = dir;
    }

    @Override
    public GlowScoreboard readMainScoreboard() throws IOException {
        return NbtScoreboardIoReader.readMainScoreboard(new File(dir, SCOREBOARD_SAVE_FILE));
    }

    @Override
    public void writeMainScoreboard(GlowScoreboard scoreboard) throws IOException {
        NbtScoreboardIoWriter.writeMainScoreboard(new File(dir, SCOREBOARD_SAVE_FILE), scoreboard);
    }

    @Override
    public void unload() throws IOException {
        save();
    }

    @Override
    public void save() throws IOException {
        writeMainScoreboard(server.getScoreboardManager().getMainScoreboard());
    }
}
