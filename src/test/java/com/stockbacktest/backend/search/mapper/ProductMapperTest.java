package com.stockbacktest.backend.search.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class ProductMapperTest {

  @Autowired
  private ProductMapper productMapper;

  @ParameterizedTest
  @CsvSource(value = {
      "tiger;KR7360750004|KR7133690008|KR7227550001|KR7458760006",
      "미국나스닥;KR7133690008",
      "110;KR7227550001",
      "KR7227550001;KR7227550001"
  }, delimiter = ';')
  @DisplayName("파라미터로 주어진 keyword가 isin_cd, isin_shrt_cd, security_name에 포함된 결과가 조회 결과로 반환된다.")
  void Given_Keyword_When_SearchProductByKeyword_Then_Returns_Product(String keyword,
      String expected) {
    // Arrange
    List<String> expectedList = Arrays.stream(expected.split("\\|")).toList();

    // Action
    List<String> products = productMapper.searchProductByKeyword(keyword);

    // Assert
    assertThat(products.size()).isEqualTo(expectedList.size());
    assertThat(products).containsExactlyInAnyOrderElementsOf(expectedList);
  }

}