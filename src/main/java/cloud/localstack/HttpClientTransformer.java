package cloud.localstack;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

public class HttpClientTransformer implements ClassFileTransformer {
    private static final List<String> TARGET_CLASSES = Arrays.asList(
            "software.amazon.awssdk.http.apache.ApacheHttpClient$DefaultBuilder",
            "software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient$DefaultBuilder",
            "software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient$DefaultBuilder",
            "software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient$DefaultBuilder"
    );

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        final String replacedClassName = className.replace("/", ".");
        if (TARGET_CLASSES.contains(replacedClassName)) {
            ClassPool cp = ClassPool.getDefault();
            cp.insertClassPath(new LoaderClassPath(loader));
            try {
                CtClass cc = cp.get(replacedClassName);
                CtMethod method = cc.getDeclaredMethod("buildWithDefaults");
                method.insertBefore("$1 = $1.merge(software.amazon.awssdk.utils.AttributeMap.builder().put(software.amazon.awssdk.http.SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES, Boolean.TRUE).build());");
                return cc.toBytecode();
            } catch (NotFoundException e) {
                System.out.println("Could not find class or method " + e.getMessage());
            } catch (CannotCompileException e) {
                System.out.println("Could not compile: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not convert to byte code: " + e.getMessage());
            }
        }
        return null;
    }
}
