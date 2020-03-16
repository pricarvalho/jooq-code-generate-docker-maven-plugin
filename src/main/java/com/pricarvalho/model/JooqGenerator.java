package com.pricarvalho.model;

import lombok.Builder;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;

@Builder
public class JooqGenerator {

    private static final String CODEGEN_DEFAULT_GENERATOR = "org.jooq.codegen.DefaultGenerator";

    private static final SystemStreamLog LOGGER = new SystemStreamLog();

    private final DockerContainer dockerContainer;

    private final Configuration jooqConfiguration;

    public void start() throws MojoExecutionException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LOGGER.info("Starting database container");

        DockerContainerUp databaseContainer = this.dockerContainer.up();
        try {
            LOGGER.info("Starting Flyway migration");
            databaseContainer.runMigrationScripts();
            LOGGER.info("Finish Flyway migration");

            LOGGER.info("Starting GenerationTool");
            GenerationTool.generate(jooqConfiguration);
            LOGGER.info("Finish GenerationTool");

        } catch (Exception ex) {
            LOGGER.error(ex);
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        } finally {
            databaseContainer.kill();
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }

}
