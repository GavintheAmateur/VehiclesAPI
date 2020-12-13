package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.management.ServiceNotFoundException;
import java.util.List;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public PriceClient(WebClient pricing, RestTemplate restTemplate) {

        this.client = pricing;
        this.restTemplate = restTemplate;
    }

    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time

    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     *
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     * error message that the vehicle ID is invalid, or note that the
     * exception is down.
     */
    public String getPrice(Long vehicleId) {


        try {

            List<ServiceInstance> instances = discoveryClient.getInstances("pricing-service");
            if (instances.size() < 1) {
                throw new ServiceNotFoundException("no available price service");
            }

            String serviceUri = String.format("%s/prices/%s", instances.get(0).getUri().toString(), vehicleId);

            Price price = restTemplate.exchange(
                    serviceUri,
                    HttpMethod.GET,
                    null,
                    Price.class
            )
                    .getBody();

            log.info("Got price from price client: " + price);

            return price.toString();

        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
            return "null";
        }

    }
}
