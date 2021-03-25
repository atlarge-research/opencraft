package science.atlarge.opencraft.opencraft.serverless;

import science.atlarge.opencraft.opencraft.GlowServer;

import java.util.HashMap;

public class SimulatedRegistry {

    class SimulationEntry{
        BlockSet blockSet;
        int simulationIndex;

        SimulationEntry(BlockSet blockSet){
            this.blockSet = blockSet;
            this.simulationIndex = 0;
        }
    }

    public class QueryResult{
        int id;
        int simulationIndex;

        QueryResult(int id, int simulationIndex){
            this.id = id;
            this.simulationIndex = simulationIndex;
        }

        public int getId() {
            return id;
        }

        public int getSimulationIndex() {
            return simulationIndex;
        }

        @Override
        public String toString() {
            return "QueryResult{" +
                    "id=" + id +
                    ", simulationIndex=" + simulationIndex +
                    '}';
        }
    }

    static SimulatedRegistry instance;

    public static SimulatedRegistry getInstance(){
        if (instance == null){
            instance = new SimulatedRegistry();
        }
        return instance;
    }

    SimulatedRegistry(){
        simulatedBlockSets = new HashMap<>();
    }

    HashMap<Integer, SimulationEntry> simulatedBlockSets;

    public void registerBlockSet(int id, BlockSet blockSet){
        simulatedBlockSets.put(id, new SimulationEntry(blockSet));
    }

    public void unregisterBlockSet(int id){
        simulatedBlockSets.remove(id);
    }

    //TODO(Javier): this search is very far from being optimal.
    public QueryResult queryBlock(int x, int y, int z){
        QueryResult res =
            simulatedBlockSets.entrySet().stream()
                .filter(s -> s.getValue().blockSet.contains(x,y,z))
                .map(e -> new QueryResult(e.getKey(), e.getValue().simulationIndex))
                .findAny()
                .orElse(null);

        return res;
    }

    public void update(int entryId){
        if(simulatedBlockSets.get(entryId).simulationIndex >= 35){
            unregisterBlockSet(entryId);
        }else{
            simulatedBlockSets.get(entryId).simulationIndex++;
        }
    }

}
