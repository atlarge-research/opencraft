package net.glowstone.lambda.population.serialization.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Random;
import net.glowstone.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import net.glowstone.generator.objects.trees.AcaciaTree;
import net.glowstone.generator.objects.trees.BigOakTree;
import net.glowstone.generator.objects.trees.BigTree;
import net.glowstone.generator.objects.trees.BirchTree;
import net.glowstone.generator.objects.trees.BrownMushroomTree;
import net.glowstone.generator.objects.trees.CocoaTree;
import net.glowstone.generator.objects.trees.DarkOakTree;
import net.glowstone.generator.objects.trees.GenericTree;
import net.glowstone.generator.objects.trees.JungleBush;
import net.glowstone.generator.objects.trees.JungleTree;
import net.glowstone.generator.objects.trees.MegaJungleTree;
import net.glowstone.generator.objects.trees.MegaPineTree;
import net.glowstone.generator.objects.trees.MegaRedwoodTree;
import net.glowstone.generator.objects.trees.MegaSpruceTree;
import net.glowstone.generator.objects.trees.RedMushroomTree;
import net.glowstone.generator.objects.trees.RedwoodTree;
import net.glowstone.generator.objects.trees.SwampTree;
import net.glowstone.generator.objects.trees.TallBirchTree;
import net.glowstone.generator.objects.trees.TallRedwoodTree;
import net.glowstone.util.BlockStateDelegate;

public class TreeDecorationSerializer implements JsonSerializer<TreeDecoration> {
    @Override
    public JsonElement serialize(TreeDecoration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("weight", src.getWeight());

        GenericTree tree = src.getConstructor().apply(new Random(), new BlockStateDelegate());

        Class<?> toAdd = null;
        if (tree instanceof AcaciaTree) {
            toAdd = AcaciaTree.class;
        } else if (tree instanceof BigOakTree) {
            toAdd = BigOakTree.class;
        } else if (tree instanceof BigTree) {
            toAdd = BigTree.class;
        } else if (tree instanceof BirchTree) {
            toAdd = BirchTree.class;
        } else if (tree instanceof BrownMushroomTree) {
            toAdd = BrownMushroomTree.class;
        } else if (tree instanceof CocoaTree) {
            toAdd = CocoaTree.class;
        } else if (tree instanceof DarkOakTree) {
            toAdd = DarkOakTree.class;
        } else if (tree instanceof JungleBush) {
            toAdd = JungleBush.class;
        } else if (tree instanceof JungleTree) {
            toAdd = JungleTree.class;
        } else if (tree instanceof MegaPineTree) {
            toAdd = MegaPineTree.class;
        } else if (tree instanceof MegaJungleTree) {
            toAdd = MegaJungleTree.class;
        } else if (tree instanceof MegaSpruceTree) {
            toAdd = MegaSpruceTree.class;
        } else if (tree instanceof MegaRedwoodTree) {
            toAdd = MegaRedwoodTree.class;
        } else if (tree instanceof RedwoodTree) {
            toAdd = RedwoodTree.class;
        } else if (tree instanceof RedMushroomTree) {
            toAdd = RedMushroomTree.class;
        } else if (tree instanceof SwampTree) {
            toAdd = SwampTree.class;
        } else if (tree instanceof TallBirchTree) {
            toAdd = TallBirchTree.class;
        } else if (tree instanceof TallRedwoodTree) {
            toAdd = TallRedwoodTree.class;
        }

        obj.addProperty("constructor", toAdd != null ? toAdd.getCanonicalName() : "");
        return obj;
    }
}
