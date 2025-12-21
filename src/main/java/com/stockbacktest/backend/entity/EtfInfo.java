package com.stockbacktest.backend.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * etf_info(종목정보) 테이블에 대한 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
public class EtfInfo {

  /**
   * isin_cd(ISIN 국제 코드) PK
   */
  private String isinCode;

  /**
   * management_name(운용사)
   */
  private String managementName;

  /**
   * etf_obj_idx_name(기초지수)
   */
  private String etfObjectIndexName;

  /**
   * tax_type(과세유형)
   */
  private String taxType;
  
  /**
   * created_at(레코드 생성 시간)
   */
  private LocalDateTime createdAt;

  /**
   * modified_at(레코드 수정 시간)
   */
  private LocalDateTime modifiedAt;
}
