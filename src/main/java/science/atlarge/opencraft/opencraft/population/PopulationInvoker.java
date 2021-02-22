package science.atlarge.opencraft.opencraft.population;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.nio.charset.StandardCharsets;

import science.atlarge.opencraft.opencraft.measurements.EventLogger;
import science.atlarge.opencraft.opencraft.measurements.EventNoopLogger;
import science.atlarge.opencraft.opencraft.population.PopulateInfo.PopulateInput;
import science.atlarge.opencraft.opencraft.population.PopulateInfo.PopulateOutput;

public class PopulationInvoker {
    private final static AWSLambda client = getClient();

    private static AWSLambda getClient() {
        return getClient(System.getenv("LAMBDA_REGION"),
                System.getenv("LAMBDA_ACCESS_KEY"),
                System.getenv("LAMBDA_SECRET_KEY"));
    }

    private static AWSLambda getClient(String regionName, String accessKey, String secretKey) {
        Regions region = Regions.fromName(regionName);
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region);

        return builder.build();
    }

    public static PopulateOutput invoke(PopulateInput input) {
        return invoke(input, new EventNoopLogger());
    }

    public static PopulateOutput invoke(PopulateInput input, EventLogger logger) {
        // for async invoke: https://stackoverflow.com/questions/47345365/how-to-invoke-aws-lambda-function-from-another-lambda-function-and-return-withou/47350875
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("NaivePopulate")
                .withPayload(input.serialize());

        // log time spent in lambda and invoke the function
        logger.start(String.format("lambda_time (%d,%d)", input.x, input.z));
        InvokeResult response = client.invoke(req);
        logger.stop(String.format("lambda_time (%d,%d)", input.x, input.z));

        // get text of response
        String serializedResponse = StandardCharsets.UTF_8.decode(response.getPayload()).toString();

        // deserialize
        return PopulateOutput.deserialize(serializedResponse);
    }

    public static void preventLambdaColdBoot() {
        // async invoke
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("NaivePopulate")
                .withInvocationType(InvocationType.Event)
                .withPayload("\"\"");
        client.invoke(req);
    }
}
