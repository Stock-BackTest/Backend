package com.stockbacktest.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * products_daily_price_info(일별종목시세정보) 테이블에 대한 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
public class ProductDailyPriceInfo {

  /**
   * isin_cd(ISIN 국제 코드) PK
   */
  private String isinCode;

  /**
   * base_date(기준일) PK
   */
  private LocalDate baseDate;

  /**
   * mrkt_price(시가)
   */
  private Integer marketPrice;

  /**
   * highest_price(고가)
   */
  private Integer highestPrice;

  /**
   * lowest_price(저가)
   */
  private Integer lowestPrice;

  /**
   * close_price(종가)
   */
  private Integer closePrice;

  /**
   * trading_quantity(거래량)
   */
  private Integer tradingQuantity;

  /**
   * trading_price(거래대금)
   */
  private Long tradingPrice;

  /**
   * shares_outstanding(상장주식수)
   */
  private Long sharesOutstanding;

  /**
   * mrkt_total_amount(시가총액)
   */
  private Long marketTotalAmount;

  /**
   * created_at(레코드 생성 시간)
   */
  private LocalDateTime createdAt;

  /**
   * modified_at(레코드 수정 시간)
   */
  private LocalDateTime modifiedAt;
}
