package com.pricarvalho.model;

import lombok.Builder;
import org.flywaydb.core.Flyway;

import java.util.List;
import java.util.Optional;

@Builder
public class Database {

    private final Optional<Integer> bindingPort;

    private final Provider provider;

    private String name = "jooq";

    private String schema = "jooq";

    private String[] scriptLocations;

    public String url() {
        return this.provider.urlPrefix()+ "localhost:" + exposedPort() + "/jooq";
    }

    public int exposedPort() {
        return this.bindingPort.orElse(port());
    }

    public int port() {
        return this.provider.port();
    }

    public String user() {
        return this.provider.user();
    }

    public String password() {
        return this.provider.password();
    }

    public List<String> environments() {
        return this.provider.variables();
    }

    public String providerLabel() {
        return this.provider.name().toLowerCase();
    }

    public Flyway flyway() {
        return Flyway.configure().locations(flywayLocations())
                     .connectRetries(30)
                     .dataSource(url(),
                                 user(),
                                 password()).load();
    }

    private String[] flywayLocations() {
        if (this.scriptLocations == null) {
            String[] migrations = new String[1];
            migrations[0] = "filesystem:src/main/resources/db/migration";
            return migrations;
        }
        String[] migrations = new String[scriptLocations.length];
        for (int i = 0; i < migrations.length; i++) {
            String location = scriptLocations[i];
            migrations[i] = "filesystem:" + location;
        }
        return migrations;
    }

    public String driver() {
        return this.provider.driver();
    }
}
