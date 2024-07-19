package com.example.shopping.service;

import com.example.shopping.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverApiService {

  private final RestTemplate restTemplate;

  // RestTemplate 주입
  public NaverApiService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  public List<ItemDto> searchItems(String query) {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/shop.json")
            .queryParam("display", 3) // 3개의 데이터를 받아온다
            .queryParam("query", query)
            .encode()
            .build()
            .toUri(); // UriComponentsBuilder에 입력한 데이터를 URI로 변환

    RequestEntity<Void> requestEntity = RequestEntity
            .get(uri)
            // Naver에서 발급받은 Client ID
            .header("X-Naver-Client-Id", "cMQm68CUzx_USWULztuV")
            // Naver에서 발급받은 Client Secret
            .header("X-Naver-Client-Secret", "Fnn5VZjRxJ")
            .build();

    // .exchange(RequestEntity, ResponseType)
    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

    return fromJSONtoItems(responseEntity.getBody());
  }

  // Naver에서 받아온 items데이터를 ItemDto 객체로 변환하여 리스트로 반환
  public List<ItemDto> fromJSONtoItems(String responseEntity) {
    JSONObject jsonObject = new JSONObject(responseEntity);
    JSONArray items  = jsonObject.getJSONArray("items");
    List<ItemDto> itemDtoList = new ArrayList<>();

    for (Object item : items) {
      ItemDto itemDto = new ItemDto((JSONObject) item);
      itemDtoList.add(itemDto);
    }

    return itemDtoList;
  }
}