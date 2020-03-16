package com.pricarvalho.model;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.Builder;

import java.util.Optional;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

@Builder
public class DockerContainer {

    private static final String CONTAINER_NAME = "jooq-flyway-docker-maven-plugin";

    private final String version;

    private final Boolean stopContainerOnComplete;

    private final Boolean removeContainerOnComplete;

    private final Database database;

    public DockerContainerUp up() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
        ExposedPort tcp22 = ExposedPort.tcp(database.port());
        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding.bindPort(database.exposedPort()));
        CreateContainerResponse jooq_mysql = dockerClient.createContainerCmd(image())
                                                         .withName(CONTAINER_NAME)
                                                         .withEnv(database.environments())
                                                         .withExposedPorts(tcp22)
                                                         .withHostConfig(newHostConfig()
                                                                                 .withPortBindings(portBindings)
                                                                                 .withPublishAllPorts(true)).exec();
        dockerClient.startContainerCmd(jooq_mysql.getId()).exec();

        return DockerContainerUp.builder()
                                .client(dockerClient)
                                .database(database)
                                .container(jooq_mysql)
                                .stopContainerOnComplete(this.stopContainerOnComplete)
                                .removeContainerOnComplete(this.removeContainerOnComplete)
                                .build();
    }

    public String image() {
        return Optional.ofNullable(this.version)
                       .map(version -> this.database.providerLabel() + ":" + version)
                       .orElse(this.database.providerLabel());
    }

    public Database database() {
        return this.database;
    }
}
