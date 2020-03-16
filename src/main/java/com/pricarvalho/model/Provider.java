package com.pricarvalho.model;

import org.flywaydb.core.internal.jdbc.DriverDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum Provider {

    MYSQL(3306, "jdbc:mysql://", "com.mysql.jdbc.Driver", "root", "root", "org.jooq.meta.mysql.MySQLDatabase"){
        @Override
        public List<String> variables() {
            List<String> defaultVariables = new ArrayList<>();
            defaultVariables.add("MYSQL_DATABASE=jooq");
            defaultVariables.add("MYSQL_ROOT_USER="+this.user());
            defaultVariables.add("MYSQL_ROOT_PASSWORD="+this.password());
            defaultVariables.add("MYSQL_DATA=\"$HOME/.dbases/jooq\"");
            return defaultVariables;
        }
    };

    private final int port;
    private final String urlPrefix;
    private final String driver;
    private final String user;
    private final String passaword;
    private final String meta;

    Provider(int port, String urlPrefix, String driver, String user, String passaword, String meta) {
        this.port = port;
        this.urlPrefix = urlPrefix;
        this.driver = driver;
        this.user = user;
        this.passaword = passaword;
        this.meta = meta;
    }

    public int port() {
        return port;
    }

    public String urlPrefix() {
        return urlPrefix;
    }

    public abstract List<String> variables();

    public String password() {
        return this.passaword;
    }

    public String user() {
        return this.user;
    }

    public String driver() {
        return this.driver;
    }

    public String meta() {
        return this.meta;
    }

}
