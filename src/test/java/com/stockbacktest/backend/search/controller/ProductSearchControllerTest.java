package com.stockbacktest.backend.search.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.stockbacktest.backend.common.web.ApiResponse;
import com.stockbacktest.backend.search.service.ProductSearchService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductSearchControllerTest {

  @Mock
  private ProductSearchService productSearchService;

  @InjectMocks
  private ProductSearchController productSearchController;

  @ParameterizedTest
  @CsvSource(value = {
      "tiger;KR7360750004|KR7133690008|KR7227550001|KR7458760006",
      "미국나스닥;KR7133690008",
      "110;KR7227550001",
      "KR7227550001;KR7227550001"
  }, delimiter = ';')
  @DisplayName("파라미터로 주어진 keyword가 isin_cd, isin_shrt_cd, security_name에 포함된 결과가 조회 결과로 반환된다.")
  void Given_Keyword_When_SearchProductByKeyword_Then_Returns_Product(String keyword,
      String expected) throws Exception {
    // Arrange
    List<String> expectedList = Arrays.stream(expected.split("\\|")).toList();
    given(productSearchService.searchProductByKeyword(keyword)).willReturn(expectedList);

    // Action
    ApiResponse result = productSearchController.search(keyword);

    // Assert
    List<String> products = (List<String>) result.data();
    assertThat(products).containsExactlyInAnyOrderElementsOf(expectedList);
  }
}