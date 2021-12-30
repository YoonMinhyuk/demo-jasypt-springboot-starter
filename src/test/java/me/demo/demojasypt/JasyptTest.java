package me.demo.demojasypt;

import me.demo.demojasypt.sharing.properties.PrivateKeyProperties;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        // DEV
//        "jasypt.encryptor.algorithm=PBEWithMD5AndDes",
//        "jasypt.encryptor.password=devPass",

        // Prod
//        "jasypt.encryptor.algorithm=PBEWITHHMACSHA512ANDAES_256",
//        "jasypt.encryptor.password=prodPass",
})
@ActiveProfiles("test")
//@ActiveProfiles("dev")
//@ActiveProfiles("prod")
public class JasyptTest {
    @Autowired
    private StringEncryptor encryptor;

    @Autowired
    private PrivateKeyProperties privateKeyProperties;

    private final String PRIVATE_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Test
    @DisplayName("PRIVATE_KEY 가 암호화되어지고 암호화된 값이 복호화되어졌을 때 복호화되어진 값은 PRIVATE_KEY 값과 같아야한다.")
    void testDecryptedPrivateKeyIsEqualsToPRIVATE_KEY() {
        //given
        final String encryptedKey = encryptor.encrypt(PRIVATE_KEY);

        /*
         * 여기서 생성된 암호화 값을 application.yml 에 "ENC(encryptedKey)" 형태로 작성
         * ConfigurationProperties 이용시 해당 Properties 의 형태에 맞게 yml 형식을 잡아 값을 넣어주면 됌
         * application.yml 참고
         **/
        System.out.println(encryptedKey);

        //when
        final String decryptedPrivateKey = encryptor.decrypt(encryptedKey);

        //then
        assertThat(decryptedPrivateKey).isEqualTo(PRIVATE_KEY);
    }

    @Test
    @DisplayName("ConfigurationProperties 인 privateKeyProperties 의 값은 복호화되어 주입되어야 한다.")
    void testPrivateKeyPropertiesValueEqualsToPRIVATE_KEY() {
        //given when
        final String privateKeyPemValue = privateKeyProperties.getPrivateKeyPem();

        //then
        /*
         * jasypt 를 통해 암호화된 값은 실제 값이 주입될 때 복호화된 값으로 주입되어진다.
         * 따라서 PrivateKeyProperties 에 privateKeyPem 의 값이 주입되어질 때는 복호화된 값이어야한다.
         * 즉, 실제 런타임에 값을 사용할 때는 PRIVATE_KEY 와 값이 같아야한다.
         **/
        assertThat(privateKeyPemValue).isEqualTo(PRIVATE_KEY);
    }

    @Test
    void testGenerateSignedUrl() {
        //given
        final String somethingValue = "somethingValue";

        //when
        final String signedUrl = privateKeyProperties.generateSignedUrl(somethingValue);

        //then
        assertThat(signedUrl).isEqualTo(privateKeyProperties.getPrivateKeyPem().concat(":").concat(somethingValue));
    }
}
