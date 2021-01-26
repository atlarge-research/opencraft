package science.atlarge.opencraft.opencraft.population.serialization.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Random;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.AcaciaTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BigOakTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BigTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BirchTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BrownMushroomTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.CocoaTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.DarkOakTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.GenericTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.JungleBush;
import science.atlarge.opencraft.opencraft.generator.objects.trees.JungleTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaJungleTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaPineTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaRedwoodTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaSpruceTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.RedMushroomTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.RedwoodTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.SwampTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.TallBirchTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.TallRedwoodTree;
import science.atlarge.opencraft.opencraft.util.BlockStateDelegate;

public class TreeDecorationSerializer implements JsonSerializer<TreeDecoration> {
    @Override
    public JsonElement serialize(TreeDecoration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("weight", src.getWeight());

        GenericTree tree = src.getConstructor().apply(new Random(), new BlockStateDelegate());

        if (tree == null) {
            return null;
        }

        Class<?> toAdd;
        if (tree instanceof AcaciaTree) {
            toAdd = AcaciaTree.class;
        } else if (tree instanceof BigTree) {
            toAdd = BigTree.class;
        } else if (tree instanceof BigOakTree) {
            toAdd = BigOakTree.class;
        } else if (tree instanceof TallBirchTree) {
            toAdd = TallBirchTree.class;
        } else if (tree instanceof BirchTree) {
            toAdd = BirchTree.class;
        } else if (tree instanceof RedMushroomTree) {
            toAdd = RedMushroomTree.class;
        } else if (tree instanceof BrownMushroomTree) {
            toAdd = BrownMushroomTree.class;
        }  else if (tree instanceof SwampTree) {
            toAdd = SwampTree.class;
        } else if (tree instanceof CocoaTree) {
            toAdd = CocoaTree.class;
        } else if (tree instanceof DarkOakTree) {
            toAdd = DarkOakTree.class;
        } else if (tree instanceof JungleBush) {
            toAdd = JungleBush.class;
        } else if (tree instanceof JungleTree) {
            toAdd = JungleTree.class;
        } else if (tree instanceof MegaSpruceTree) {
            toAdd = MegaSpruceTree.class;
        } else if (tree instanceof MegaPineTree) {
            toAdd = MegaPineTree.class;
        } else if (tree instanceof MegaRedwoodTree) {
            toAdd = MegaRedwoodTree.class;
        } else if (tree instanceof MegaJungleTree) {
            toAdd = MegaJungleTree.class;
        } else if (tree instanceof TallRedwoodTree) {
            toAdd = TallRedwoodTree.class;
        } else if (tree instanceof RedwoodTree) {
            toAdd = RedwoodTree.class;
        } else {
            toAdd = GenericTree.class;
        }

        obj.addProperty("constructor", toAdd.getCanonicalName());
        return obj;
    }
}
