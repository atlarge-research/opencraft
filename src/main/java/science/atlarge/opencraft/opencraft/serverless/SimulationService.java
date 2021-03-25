package science.atlarge.opencraft.opencraft.serverless;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface SimulationService {
    @LambdaFunction(functionName = "hello-lamda")
    String[] call(String input);

}
