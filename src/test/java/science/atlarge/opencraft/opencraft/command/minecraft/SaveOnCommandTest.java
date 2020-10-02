package science.atlarge.opencraft.opencraft.command.minecraft;

import science.atlarge.opencraft.opencraft.command.CommandTest;

public class SaveOnCommandTest extends CommandTest<SaveToggleCommand> {

    // TODO: Add more tests.

    public SaveOnCommandTest() {
        super(() -> new SaveToggleCommand(true));
    }
}

