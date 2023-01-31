package org.jupiterhub.pipu.chat.util;

import java.util.Optional;

public class EnvironmentUtil {
    public static String getHostname() {
        return Optional.ofNullable(System.getenv("HOSTNAME")).orElse("localhost");
    }

    public static boolean isSameHost(String hostname) {
        return getHostname().equals(hostname);
    }
}
