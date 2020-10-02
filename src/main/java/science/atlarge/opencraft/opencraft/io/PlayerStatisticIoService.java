package science.atlarge.opencraft.opencraft.io;

import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public interface PlayerStatisticIoService {

    /**
     * Reads and populates the statistics of the player.
     *
     * @param player The player.
     */
    void readStatistics(GlowPlayer player);

    /**
     * Saves the statistics of the player.
     *
     * @param player The player.
     */
    void writeStatistics(GlowPlayer player);
}
