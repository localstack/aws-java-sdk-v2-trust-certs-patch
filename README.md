# AWS Java SDK v2 - Trust all Certificates Patch

## Overview

This repository contains the code to a small java agent, which will disable the certificate name validation for your [AWS Java SDK v2](https://github.com/aws/aws-sdk-java-v2) clients.

This tool was made necessary due to the decision of the AWS Java SDK team to remove the global configuration option for this functionality with the AWS SDK v2. See: aws/aws-sdk-java-v2#1230

For the AWS Java SDK v1, please set the [`-Dcom.amazonaws.sdk.disableCertChecking`](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/SDKGlobalConfiguration.html#DISABLE_CERT_CHECKING_SYSTEM_PROPERTY) system property.

**Warning**: Please note that the usage of this tool is meant for testing/development purposes only.
Please do not disable certificate name validation on your production stack.

## Usage

1) Download the latest [release](https://github.com/localstack/aws-java-sdk-v2-trust-certs-patch/releases)
2) Load the jar file as java agent using `-javaagent:<path-to-file>` either specified as command line argument, or setting it in the `JAVA_TOOL_OPTIONS` environment variable when starting your process like this: `JAVA_TOOL_OPTIONS=-javaagent:<path-to-file>`.
3) You can now use for example DNS to redirect your SDK calls to any endpoint you want, e.g. to [LocalStack](https://github.com/localstack/localstack).

Please remember to use a separate profile/configuration for your development and production environments, to avoid accidentally disabling the certificate name verification in production.

## How does it work?

This utility works by using [java instrumentation](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html) to set the [`TRUST_ALL_CERTIFICATES`](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/SdkHttpConfigurationOption.html#TRUST_ALL_CERTIFICATES) option per default on all created clients.
It does so by merging passed AttributeMaps of the SDK with a new one setting this option in the `buildWithDefaults` methods of all client builders.

This tool uses [javassist](https://www.javassist.org/) to insert and compile the bytecode on the load of the respective client classes.

## Supported HTTP Clients

This tools supports the following http clients, if used:

Synchronous:

* [ApacheHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/apache/ApacheHttpClient.html)
* [UrlConnectionHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/urlconnection/UrlConnectionHttpClient.html)

Asynchronous:

* [NettyNioAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/nio/netty/NettyNioAsyncHttpClient.html)
* [AwsCrtAsyncHttpClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/http/crt/AwsCrtAsyncHttpClient.html)