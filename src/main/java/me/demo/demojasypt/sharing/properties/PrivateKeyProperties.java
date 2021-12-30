package me.demo.demojasypt.sharing.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("secret")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
@ToString
public class PrivateKeyProperties {
    private final String privateKeyPem;

    public String generateSignedUrl(final String something) {
        return privateKeyPem.concat(":").concat(something);
    }
}
