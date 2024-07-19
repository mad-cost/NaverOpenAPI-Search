package com.example.shopping.controller;

import com.example.shopping.dto.ItemDto;
import com.example.shopping.service.NaverApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NaverApiController {

  private final NaverApiService naverApiService;

  public NaverApiController(NaverApiService naverApiService) {
    this.naverApiService = naverApiService;
  }

  @GetMapping("/")
  public String root(){
    return "index";
  }

  @GetMapping("/search")
  public String searchItems(
          @RequestParam
          String query,
          Model model
  )  {
    List<ItemDto> itemDtos = naverApiService.searchItems(query);
    model.addAttribute("items", itemDtos);

    return "index";
  }

}