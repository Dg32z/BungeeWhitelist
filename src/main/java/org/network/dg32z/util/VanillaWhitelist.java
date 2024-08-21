package org.network.dg32z.util;

public final class VanillaWhitelist {

    private final String uuid, name;

    public VanillaWhitelist(final String uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
