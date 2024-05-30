package com.weatherapi.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    private RestTemplate restTemplate;


    public double[] getLatLongFromPincode(String pincode) throws Exception {
        String url = String.format("https://india-pincode-with-latitude-and-longitude.p.rapidapi.com/api/v1/pincode/%s", pincode);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", "india-pincode-with-latitude-and-longitude.p.rapidapi.com");
        headers.set("x-rapidapi-key", rapidApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);


        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        if (root.isArray() && !root.isEmpty()) {
            JsonNode location = root.get(root.size() - 1);  // Get the last location
            double lat = location.path("lat").asDouble();
            double lng = location.path("lng").asDouble();
            return new double[]{lat, lng};
        } else {
            throw new Exception("No location found for the given pincode");
        }

//        String body = response.getBody();
//        JSONObject jsonObject = new JSONObject(response);
//        if (jsonObject.getString("status").equals("OK")) {
//            JSONObject location = jsonObject.getJSONArray("results")
//                    .getJSONObject(0)
//                    .getJSONObject("geometry")
//                    .getJSONObject("location");
//            double lat = location.getDouble("lat");
//            double lng = location.getDouble("lng");
//            return new double[]{lat, lng};
//        }
    }
}
