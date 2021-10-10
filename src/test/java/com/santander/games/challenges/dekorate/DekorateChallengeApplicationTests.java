package com.santander.games.challenges.dekorate;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import io.dekorate.utils.Serialization;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.DeploymentConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.CoreMatchers.is;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DekorateChallengeApplicationTests {

	@LocalServerPort
	int port;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void testHelloEndpoint() {
		given()
				.when().get("/")
				.then()
				.statusCode(200)
				.body(is("Hello world"));
	}

    @Test
    public void shouldHaveMatchingOutputImageAndTrigger() {
        KubernetesList list = Serialization.unmarshalAsList(getClass().getClassLoader().getResourceAsStream("META-INF/dekorate/openshift.yml"));
        assertNotNull(list);
        DeploymentConfig d = findFirst(list, DeploymentConfig.class).orElseThrow(() -> new IllegalStateException());
        BuildConfig b = findFirst(list, BuildConfig.class).orElseThrow(() -> new IllegalStateException());
        assertNotNull(d);
        assertNotNull(b);
        assertTrue(d.getSpec().getTriggers().stream().filter(t -> t.getImageChangeParams().getFrom().getName().equals(b.getSpec().getOutput().getTo().getName())).findFirst().isPresent());
    }

    <T extends HasMetadata> Optional<T> findFirst(KubernetesList list, Class<T> t) {
        return (Optional<T>) list.getItems().stream()
        .filter(i -> t.isInstance(i))
        .findFirst();
    }

}
