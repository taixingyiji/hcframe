package org.springframework.boot.autoconfigure.jdbc;

import org.springframework.context.annotation.Configuration;

/**
 * Compatibility shim: provide the legacy class name expected by some third-party starters
 * (e.g. druid-spring-boot-3-starter) that reference org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.
 *
 * This class intentionally delegates to the new Spring Boot package (org.springframework.boot.jdbc.DataSourceAutoConfiguration)
 * via having the same name in the legacy package so Class.forName succeeds.
 */
@Configuration
public class DataSourceAutoConfiguration {
    // Intentionally empty - presence is sufficient for compatibility with older starters.
}
