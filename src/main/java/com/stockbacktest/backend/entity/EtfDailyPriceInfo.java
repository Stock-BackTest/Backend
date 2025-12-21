package com.stockbacktest.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * etfs_daily_price_info(일별종목시세정보) 테이블에 대한 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
public class EtfDailyPriceInfo {

  /**
   * isin_cd(ISIN 국제 코드) PK
   */
  private String isinCode;

  /**
   * base_date(기준일) PK
   */
  private LocalDate baseDate;

  /**
   * total_net_assets(ETF 순자산총액)
   */
  private Long totalNetAsseets;

  /**
   * nav(순자산가치)
   */
  private Long nav;

  /**
   * created_at(레코드 생성 시간)
   */
  private LocalDateTime createdAt;

  /**
   * modified_at(레코드 수정 시간)
   */
  private LocalDateTime modifiedAt;
}
