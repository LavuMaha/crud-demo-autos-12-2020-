package com.example.autos;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations= "classpath:application-test.properties")
class AutosCrudApiDemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AutosRepository autosRepository;

	private List<Automobile> testAutos;

	@BeforeEach
	void setup() {
		testAutos = sampleAutos(10);
		autosRepository.saveAll(testAutos);
	}

	@AfterEach
		//Cleanup
	void tearDown() {
		autosRepository.deleteAll();
	}

	@Test
	void canPostNewAuto_returnsNewAutoDetails() {
		// Arrange
		Automobile automobile = new Automobile();
		automobile.setVin("ABC123XX");
		automobile.setYear("1995");
		automobile.setMake("Ford");
		automobile.setModel("Windstar");
		automobile.setColor("Blue");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

		// Act
		ResponseEntity<Automobile> response = restTemplate.postForEntity("/autos", request, Automobile.class);

		// Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());

	}

	@Test
	void searchAutos_all_returnsListDetails() {
		ResponseEntity<AutosList> response = restTemplate.getForEntity("/autos", AutosList.class);

		AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		AssertionsForClassTypes.assertThat(response.getBody().getAutomobiles().isEmpty()).isFalse();
	}

	@Test
	void searchAutos_green_returnsListGreenDetails() {
		//Search is case sensitive for some reason.  Not sure why.  Maybe it's H2.  Will test later.
		ResponseEntity<AutosList> response = restTemplate.getForEntity("/autos?color=Green", AutosList.class);

		AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		AssertionsForClassTypes.assertThat(response.getBody().getAutomobiles().isEmpty()).isFalse();
		AssertionsForClassTypes.assertThat(response.getBody().getAutomobiles().get(0).getColor()).isEqualTo("Green");

	}

	@Test
	void getAutoByVin_returnsDetails() {
		Automobile expected = testAutos.get(3);
		ResponseEntity<Automobile> response = restTemplate.getForEntity("/autos/"+expected.getVin(), Automobile.class);

		AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expected);

	}

	@Test
	void deleteAutoByVin() {
		String vin = testAutos.get(0).getVin();
		restTemplate.delete("/autos/"+vin);
		//Test that it was deleted
		ResponseEntity<Automobile> response = restTemplate.getForEntity("/autos/"+vin, Automobile.class);
		AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void patchAuto() {
		// Arrange
		Automobile expected = testAutos.get(5);
		Properties p = new Properties();
		p.put("name", "new owner");
		p.put("phone", "123-456-7890");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "Application/Json");
		HttpEntity request = new HttpEntity(p, headers);

		// Act - "patchRestTemplate, defined above is for the work-a-round"
		ResponseEntity response =
				restTemplate.exchange("/autos/"+expected.getVin(), HttpMethod.PATCH,
						request, Automobile.class);

		//Assert
		AssertionsForClassTypes.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		ResponseEntity<Automobile> response2 = restTemplate.getForEntity("/autos/"+expected.getVin(), Automobile.class);
		AssertionsForClassTypes.assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
		AssertionsForClassTypes.assertThat(response2.getBody().getStatus()).isEqualTo("sold");
	}

	private List<Automobile> sampleAutos(int qty){
		List<Automobile> automobiles = new ArrayList<>();
		Automobile auto;
		for (int i = 0; i < qty; i++) {
			auto = new Automobile();
			auto.setVin("ABC123XX"+i);
			auto.setYear("190"+i);
			auto.setMake("Make-"+i);
			auto.setModel("Model-"+i);
			auto.setColor(i%2==0 ? "Red" : "Green");
			auto.setStatus("Available");
			automobiles.add(auto);
		}
		return automobiles;
	}


}
