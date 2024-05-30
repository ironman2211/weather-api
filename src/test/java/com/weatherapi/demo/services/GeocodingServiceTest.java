package com.weatherapi.demo.services;
import com.weatherapi.demo.dto.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeocodingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetLatLongFromPINCode_Success() throws Exception {
        String pincode = "123456";
        String responseBody = "[{\"pincode\":\"123456\",\"city\":\"CityName\",\"state\":\"StateName\",\"lat\":12.3456,\"lng\":-78.9012}]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        Coordinates coordinates = geocodingService.getLatLongFromPINCode(pincode);

        assertNotNull(coordinates);
        assertEquals(12.3456, coordinates.getLatitude());
        assertEquals(-78.9012, coordinates.getLongitude());
    }

    @Test
    void testGetLatLongFromPINCode_NoLocationFound() {
        String pincode = "123456";
        String responseBody = "[]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        assertThrows(Exception.class, () -> geocodingService.getLatLongFromPINCode(pincode));
    }
}
