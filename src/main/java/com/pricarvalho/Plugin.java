package com.pricarvalho;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static org.jooq.Constants.XSD_CODEGEN;

@Mojo(name = "bla", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class Plugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    boolean skip = false;

    @Parameter(required = true)
    GeneratorMojo generator;

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Skipping jOOQ code generation");
            return;
        }
        if (generator == null) {
            getLog().error("Incorrect configuration of jOOQ code generation tool");
            getLog().error(
                    "\n"
                            + "The jOOQ-codegen-maven module's generator configuration is not set up correctly.\n"
                            + "This can have a variety of reasons, among which:\n"
                            + "- Your pom.xml's <configuration> contains invalid XML according to " + XSD_CODEGEN + "\n"
                            + "- There is a version or artifact mismatch between your pom.xml and your commandline");

            throw new MojoExecutionException("Incorrect configuration of jOOQ code generation tool. See error above for details.");
        }

        this.generator.getJooqGeneratorInstance().start();

        project.addCompileSourceRoot(this.generator.target().getDirectory());
    }




}

