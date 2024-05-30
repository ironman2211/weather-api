package com.weatherapi.demo.services;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.demo.dto.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.weatherapi.demo.constants.WeatherApiConstants.RAPID_API_HOST;
import static com.weatherapi.demo.constants.WeatherApiConstants.RAPID_API_KEY;

@Service
public class GeocodingService {

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    @Autowired
    private RestTemplate restTemplate;

    public Coordinates getLatLongFromPINCode(String pincode) throws Exception {
        String url = String.format("https://india-pincode-with-latitude-and-longitude.p.rapidapi.com/api/v1/pincode/%s", pincode);

        HttpHeaders headers = new HttpHeaders();
        headers.set(RAPID_API_HOST, rapidApiHost);
        headers.set(RAPID_API_KEY, rapidApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        if (root.isArray() && !root.isEmpty()) {
            JsonNode location = root.get(root.size() - 1);  // Get the last location
            return Coordinates.builder()
                    .latitude(location.path("lat").asDouble())
                    .longitude(location.path("lng").asDouble()).build();
        } else {
            throw new Exception("No location found for the given pin-code");
        }
    }
}
