package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void contextLoads() {
	}

	@Test
	public void testCRUDPrice() throws Exception {
		String price = "{\n" +
				" \"vehicleId\":3333,\n" +
				"  \"currency\":\"USD\",\n" +
				"  \"price\":\"3233355.00\"\n" +
				"}";

		mockMvc.perform(
				post("/prices")
						.content(price)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isCreated())
		;
		mockMvc.perform(
				get("/prices/1")
		).andExpect(status().isOk())
				.andExpect(jsonPath("currency").value("USD"))
				.andExpect(jsonPath("price").value(3233355.00))
				;
	}

}
