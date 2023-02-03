package service.impl;

import service.IAwsSecretService;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.ParameterNotFoundException;
import static settings.properties.AppPropertiesManager.getProperty;

public class AwsSecretService implements IAwsSecretService {

    SsmClient ssmClient;

    public AwsSecretService() {
        this.ssmClient = SsmClient.builder()
                .region(Region.of(getProperty("aws.region")))
                .build();
    }

    @Override
    public String getSecretByKey(String key) {
        try {
            GetParameterResponse parameter = ssmClient.getParameter(GetParameterRequest.builder()
                    .name(key)
                    .withDecryption(true)
                    .build());
            return parameter.parameter().value();
        } catch (ParameterNotFoundException ex) {
            System.err.println("Key not found");
            return "";
        }
    }
}
