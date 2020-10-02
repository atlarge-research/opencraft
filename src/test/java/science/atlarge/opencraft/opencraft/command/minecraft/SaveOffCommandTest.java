package science.atlarge.opencraft.opencraft.command.minecraft;

import science.atlarge.opencraft.opencraft.command.CommandTest;

public class SaveOffCommandTest extends CommandTest<SaveToggleCommand> {

    // TODO: Add more tests.

    public SaveOffCommandTest() {
        super(() -> new SaveToggleCommand(false));
    }
}

