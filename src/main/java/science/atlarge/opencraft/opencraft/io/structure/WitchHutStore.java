package science.atlarge.opencraft.opencraft.io.structure;

import science.atlarge.opencraft.opencraft.generator.structures.GlowWitchHut;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public class WitchHutStore extends TemplePieceStore<GlowWitchHut> {

    public WitchHutStore() {
        super(GlowWitchHut.class, "TeSH");
    }

    @Override
    public GlowWitchHut createStructurePiece() {
        return new GlowWitchHut();
    }

    @Override
    public void load(GlowWitchHut structurePiece, CompoundTag compound) {
        super.load(structurePiece, compound);
        compound.readBoolean("Witch", structurePiece::setHasWitch);
    }

    @Override
    public void save(GlowWitchHut structurePiece, CompoundTag compound) {
        super.save(structurePiece, compound);

        compound.putByte("Witch", structurePiece.getHasWitch() ? 1 : 0);
    }
}
