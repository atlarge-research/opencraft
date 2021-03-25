package science.atlarge.opencraft.opencraft.serverless;

import  com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import  com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

public class Invoker {

    private static Invoker instance;

    public static Invoker getInstance(){
        if(instance == null){
            instance = new Invoker();
        }
        return instance;
    }

    final SimulationService simulationService;

    Invoker(){
        simulationService = LambdaInvokerFactory.builder()
            .lambdaClient(
                AWSLambdaClientBuilder
                    .standard()
                    .withRegion("eu-north-1")
                    .build()
            ).build(SimulationService.class);
    }

    public String[] simulate(String input){
        return simulationService.call(input);
    }

}
