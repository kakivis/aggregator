package com.api.aggregator.client;

import com.api.aggregator.dto.HerokuContactListDto;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HerokuClient {

  private final RestTemplate restTemplate;
  private final String url;
  private final String token;

  public HerokuClient(@Value("${api.url}") String url, @Value("${api.token}") String token) {
    this.url = url;
    this.restTemplate = new RestTemplateBuilder()
        .rootUri(url)
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();
    this.token = token;
  }

  public ResponseEntity<HerokuContactListDto> getContacts(Integer page) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    headers.setBearerAuth(token);
    HttpEntity<?> requestEntity = new HttpEntity<>(headers);

    return restTemplate.exchange(url + "/contacts" + "?page=" + page,
        HttpMethod.GET,
        requestEntity,
        new ParameterizedTypeReference<>() {
        });
  }
}
