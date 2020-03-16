package com.pricarvalho.model;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import lombok.Builder;

@Builder
public class DockerContainerUp {

    private final DockerClient client;

    private final Database database;

    private final CreateContainerResponse container;

    private boolean stopContainerOnComplete;

    private boolean removeContainerOnComplete;

    public void kill() {
        if(stopContainerOnComplete) {
            client.stopContainerCmd(container.getId()).exec();
            if(removeContainerOnComplete)
                client.removeContainerCmd(container.getId()).exec();
        }
    }

    public void runMigrationScripts() {
        this.database.flyway().migrate();
    }
}
