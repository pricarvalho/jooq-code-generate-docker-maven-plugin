package com.pricarvalho;

import com.pricarvalho.model.Database;
import com.pricarvalho.model.DockerContainer;
import com.pricarvalho.model.Provider;

import java.io.Serializable;
import java.util.Optional;

public class DatabaseMojo implements Serializable {

    private DockerMojo docker;

    private String provider;

    private String version;

    private Integer port;

    private String name = "jooq";

    private String schema = "jooq";

    private String includes = ".*";

    private String excludes = "";

    private String[] scriptLocations;

    private Provider provider() {
        return Provider.valueOf(provider.toUpperCase());
    }

    public org.jooq.meta.jaxb.Database jooqDatabase() {
        return new org.jooq.meta.jaxb.Database()
                .withName(provider().meta())
                .withInputSchema(this.schema)
                .withIncludeExcludeColumns(true)
                .withExcludes(this.excludes)
                .withIncludes(this.includes);
    }

    public DockerContainer dockerContainer() {
        return DockerContainer.builder()
                              .database(Database.builder()
                                                .provider(provider())
                                                .name(this.name)
                                                .schema(this.schema)
                                                .bindingPort(Optional.ofNullable(port))
                                                .scriptLocations(this.scriptLocations)
                                                .build())
                              .version(this.version)
                              .removeContainerOnComplete(this.docker.removeContainerOnComplete)
                              .stopContainerOnComplete(this.docker.stopContainerOnComplete)
                              .build();
    }

}
