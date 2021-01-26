package science.atlarge.opencraft.opencraft.entity;

public class GlowTntPrimedTest extends GlowExplosiveTest<GlowTntPrimed> {
    public GlowTntPrimedTest() {
        super(location -> new GlowTntPrimed(location, null));
    }
}
