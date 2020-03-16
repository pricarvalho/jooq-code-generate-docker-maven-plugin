# jooq-code-generate-docker-maven-plugin

## **Usage example:**

```xml<build>
     <plugins>
         <plugin>
             <groupId>com.pricarvalho</groupId>
             <artifactId>jooq-code-generate-docker-maven-plugin</artifactId>
             <version>1.0-SNAPSHOT</version>
             <configuration>
                 <skip>false</skip>
                 <generator>
                     <database>
                         <docker>
                             <stopContainerOnComplete>true</stopContainerOnComplete>
                             <removeContainerOnComplete>true</removeContainerOnComplete>
                         </docker>
                         <provider>mysql</provider>
                         <version>5.7.26</version>
                         <port>3307</port>
                         <name>jooq</name>
                         <schema>jooq</schema>
                         <includes>.*</includes>
                         <excludes></excludes>
                         <scriptLocations>
                             <scriptLocation>src/main/resources/db/migration</scriptLocation>
                         </scriptLocations>
                     </database>
                     <target>
                         <packageName>infrastructure.sql.generated</packageName>
                         <directory>target/generated-sources/jooq</directory>
                     </target>
                     <generate>
                         <javaTimeTypes>true</javaTimeTypes>
                     </generate>
                 </generator>
             </configuration>
             <dependencies>
                 <dependency>
                     <groupId>mysql</groupId>
                     <artifactId>mysql-connector-java</artifactId>
                     <version>8.0.15</version>
                 </dependency>
             </dependencies>
         </plugin>
     </plugins>
 </build>``