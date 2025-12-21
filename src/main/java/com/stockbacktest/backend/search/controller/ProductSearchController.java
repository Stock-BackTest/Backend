package com.stockbacktest.backend.search.controller;

import com.stockbacktest.backend.common.web.ApiResponse;
import com.stockbacktest.backend.search.service.ProductSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductSearchController {

  private final ProductSearchService productSearchService;

  @GetMapping("/search")
  public ResponseEntity<ApiResponse> search(@RequestParam String keyword) throws Exception {
    List<String> result = productSearchService.searchProductByKeyword(keyword);

    return ResponseEntity.ok(ApiResponse.success(result));
  }
}
