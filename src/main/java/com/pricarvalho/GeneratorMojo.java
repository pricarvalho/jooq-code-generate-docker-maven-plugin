package com.pricarvalho;

import com.pricarvalho.model.DockerContainer;
import com.pricarvalho.model.JooqGenerator;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;

import java.util.Optional;

import static org.jooq.codegen.GenerationTool.DEFAULT_TARGET_DIRECTORY;

public class GeneratorMojo {

    DatabaseMojo database;

    Target target;

    Generate generate;

    Strategy strategy;

    public JooqGenerator getJooqGeneratorInstance() {
        DockerContainer dockerContainer = this.database.dockerContainer();
        return JooqGenerator.builder()
                            .dockerContainer(dockerContainer)
                            .jooqConfiguration(new Configuration()
                                                   .withJdbc(new Jdbc().withDriver(dockerContainer.database().driver())
                                                                       .withUrl(dockerContainer.database().url())
                                                                       .withUser(dockerContainer.database().user())
                                                                       .withPassword(dockerContainer.database().password()))
                                                   .withGenerator(new org.jooq.meta.jaxb.Generator()
                                                                          .withGenerate(this.generate)
                                                                          .withTarget(target())
                                                                          .withStrategy(this.strategy)
                                                                          .withDatabase(
                                                                                  this.database.jooqDatabase())))
                            .build();

    }

    public Target target() {
        return Optional.ofNullable(this.target)
                       .orElse(new Target().withDirectory(
                               DEFAULT_TARGET_DIRECTORY))
                       .withDirectory(DEFAULT_TARGET_DIRECTORY);
    }
}
