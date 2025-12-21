package com.stockbacktest.backend.search.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

  List<String> searchProductByKeyword(@Param("keyword") String keyword);
}
