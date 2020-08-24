package net.glowstone.lambda.population;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

import java.nio.charset.StandardCharsets;

import net.glowstone.lambda.population.serialization.PopulateInfo.PopulateInput;
import net.glowstone.lambda.population.serialization.PopulateInfo.PopulateOutput;

public class PopulationInvoker {
    private final AWSLambda client;

    public PopulationInvoker() {
        this(System.getenv("LAMBDA_REGION"),
                System.getenv("LAMBDA_ACCESS_KEY"),
                System.getenv("LAMBDA_SECRET_KEY"));
    }

    public PopulationInvoker(String regionName, String accessKey, String secretKey) {
        Regions region = Regions.fromName(regionName);

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AWSLambdaClientBuilder builder = AWSLambdaClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region);

        client = builder.build();
    }

    public PopulateOutput invoke(PopulateInput input) {
        InvokeRequest req = new InvokeRequest()
                .withFunctionName("NaivePopulate")
                .withPayload(input.toJson());
        InvokeResult response = client.invoke(req);

        // get text of response
        String serializedResponse = StandardCharsets.UTF_8.decode(response.getPayload()).toString();

        // deserialize
        return PopulateOutput.fromJson(serializedResponse);
    }
}
