package me.demo.demojasypt.config;

import me.demo.demojasypt.sharing.properties.PrivateKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        PrivateKeyProperties.class
})
public class ConfigurationPropertiesConfig {
}
