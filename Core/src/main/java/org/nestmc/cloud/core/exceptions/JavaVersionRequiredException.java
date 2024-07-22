package org.nestmc.cloud.core.exceptions;

public class JavaVersionRequiredException extends Exception {
    public JavaVersionRequiredException(){
        super("Java 8 is required to start the cloud !");
    }
}
