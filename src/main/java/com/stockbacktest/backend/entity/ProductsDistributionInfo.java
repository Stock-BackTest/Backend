package com.stockbacktest.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * products_distribution_info(분배/배당금 정보) 테이블에 대한 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
public class ProductsDistributionInfo {

  /**
   * isin_cd(ISIN 국제 코드) PK
   */
  private String isinCode;

  /**
   * base_date(기준일) PK
   */
  private LocalDate baseDate;

  /**
   * payable_date(실지급일)
   */
  private LocalDate payableDate;

  /**
   * cash_per_share(주당분배금)
   */
  private BigDecimal cashPerShare;

  /**
   * payout_yield_pct(시가대비 분배율)
   */
  private BigDecimal payoutYieldPercent;

  /**
   * tax_basis(결산과표기준)
   */
  private String taxBasis;

  /**
   * distribution_type(배당구분)
   */
  private String distributionType;

  /**
   * created_at(레코드 생성 시간)
   */
  private LocalDateTime createdAt;

  /**
   * modified_at(레코드 수정 시간)
   */
  private LocalDateTime modifiedAt;
}
