package cloud.localstack;

import java.lang.instrument.Instrumentation;


public class AwsSdkV2DisableCertificateValidation {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Disabling AWS sdk v2 certificate validation...");
        inst.addTransformer(new HttpClientTransformer());
    }
}
