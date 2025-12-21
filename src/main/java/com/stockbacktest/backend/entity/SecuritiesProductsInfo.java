package com.stockbacktest.backend.entity;

import com.stockbacktest.backend.common.enums.AssetCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * securities_products_info(종목정보) 테이블에 대한 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
public class SecuritiesProductsInfo {

  /**
   * isin_cd(ISIN 국제 코드) PK
   */
  private String isinCode;

  /**
   * isin_shrt_cd(6자리 단축코드)
   */
  private String isinShortCode;

  /**
   * seucrity_name(종목명)
   */
  private String securityName;

  /**
   * asset_cd(주식/etf 구분 코드)
   */
  private AssetCode assetCode;

  /**
   * listing_date(상장일)
   */
  private LocalDate listingDate;

  /**
   * delisting_date(상장폐지일)
   */
  private LocalDate delistingDate;

  /**
   * created_at(레코드 생성 시간)
   */
  private LocalDateTime createdAt;

  /**
   * modified_at(레코드 수정 시간)
   */
  private LocalDateTime modifiedAt;
}
