package com.stockbacktest.backend.search.service;

import com.stockbacktest.backend.search.mapper.ProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

  private final ProductMapper productMapper;

  public List<String> searchProductByKeyword(String keyword) {
    return productMapper.searchProductByKeyword(keyword);
  }

}
