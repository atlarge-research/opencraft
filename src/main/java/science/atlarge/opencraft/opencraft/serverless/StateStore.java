package science.atlarge.opencraft.opencraft.serverless;

import com.google.gson.Gson;
import science.atlarge.opencraft.opencraft.GlowServer;

import java.util.Arrays;
import java.util.HashMap;

public class StateStore {

    static StateStore instance;

    public static StateStore getInstance(){
        if (instance == null){
            instance = new StateStore();
        }
        return instance;
    }

    StateStore(){
        store = new HashMap<>();
    }

    HashMap<Integer, BlockSet[]> store;

    public void put(int blockSetId, String[] states){

        GlowServer.logger.info(
            String.format("putting in state store blockset id: %d of size: %d", blockSetId, states.length)
        );

        Gson gson = new Gson();
        BlockSet[] parsedStates = Arrays.stream(states)
                .map(state -> gson.fromJson(state, BlockSet.class))
                .toArray(BlockSet[]::new);

        store.put(blockSetId, parsedStates);

        GlowServer.logger.info(String.format("state store put success"));

    }

    public BlockSet get(int blockSetId, int n){
        GlowServer.logger.info(String.format("getting blockset w/ id: %d at state: %d", blockSetId, n));
        return store.get(blockSetId)[n];
    }

}
