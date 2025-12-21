-- =========================
-- 1) TABLES
-- =========================

create table securities_products_info (
  isin_cd         varchar(12) primary key,
  isin_shrt_cd    varchar(6)  not null,
  security_name   text        not null,
  asset_cd        text,
  listing_date    date,
  delisting_date  date,
  created_at      timestamp not null default now(),
  modified_at     timestamp not null default now()
);

create table etf_info (
  isin_cd          varchar(12) primary key,
  management_name  text,
  etf_obj_idx_name text,
  tax_type         text,
  created_at       timestamp not null default now(),
  modified_at      timestamp not null default now(),
  constraint fk_etf_info__spi
    foreign key (isin_cd)
    references securities_products_info (isin_cd)
    -- on delete cascade
);

create table products_daily_price_info (
  isin_cd            varchar(12) not null,
  base_date          date        not null,
  mrkt_price         int,
  highest_price      int,
  lowest_price       int,
  close_price        int,
  trading_quantity   int,
  trading_price      bigint,
  shares_outstanding bigint,
  mrkt_total_amount  bigint,
  created_at         timestamp not null default now(),
  modified_at        timestamp not null default now(),
  primary key (isin_cd, base_date),
  constraint fk_pdi__spi
    foreign key (isin_cd)
    references securities_products_info (isin_cd)
);

create table etfs_daily_price_info (
  isin_cd          varchar(12) not null,
  base_date        date        not null,
  total_net_assets bigint,
  nav              bigint,
  created_at       timestamp not null default now(),
  modified_at      timestamp not null default now(),
  primary key (isin_cd, base_date),
  constraint fk_edpi__pdi
    foreign key (isin_cd, base_date)
    references products_daily_price_info (isin_cd, base_date)
    -- on delete cascade
);

create table products_distribution_info (
  isin_cd           varchar(12) not null,
  base_date         date        not null,
  payable_date      date,
  cash_per_share    numeric(10,2),
  payout_yield_pct  numeric(5,2),
  tax_basis         text,
  distribution_type text,
  created_at        timestamp not null default now(),
  modified_at       timestamp not null default now(),
  primary key (isin_cd, base_date),
  constraint fk_pdis__spi
    foreign key (isin_cd)
    references securities_products_info (isin_cd)
);

-- =========================
-- 2) INDEXES
-- =========================
create index idx_spi_isin_shrt_cd on securities_products_info (isin_shrt_cd);
create index idx_spi_security_name on securities_products_info (security_name);

-- create extension if not exists pg_trgm;
-- create index idx_spi_security_name_trgm on securities_products_info using gin (security_name gin_trgm_ops);

-- =========================
-- 3) COMMENTS
-- =========================
comment on table securities_products_info is '종목정보';
comment on column securities_products_info.isin_cd is 'ISIN_국제_코드';
comment on column securities_products_info.isin_shrt_cd is '6자리 단축코드';
comment on column securities_products_info.security_name is '종목명';
comment on column securities_products_info.asset_cd is '주식/ETF 구분 코드';
comment on column securities_products_info.listing_date is '상장일';
comment on column securities_products_info.delisting_date is '상장폐지일';
comment on column securities_products_info.created_at is '레코드 생성 시간';
comment on column securities_products_info.modified_at is '레코드 수정 시간';

comment on table etf_info is 'ETF 종목정보';
comment on column etf_info.isin_cd is 'ISIN_국제_코드';
comment on column etf_info.management_name is '운용사';
comment on column etf_info.etf_obj_idx_name is '기초지수';
comment on column etf_info.tax_type is '과세유형';
comment on column etf_info.created_at is '레코드 생성 시간';
comment on column etf_info.modified_at is '레코드 수정 시간';

comment on table products_daily_price_info is '일별종목시세정보';
comment on column products_daily_price_info.isin_cd is 'ISIN_국제_코드';
comment on column products_daily_price_info.base_date is '기준일';
comment on column products_daily_price_info.mrkt_price is '시가';
comment on column products_daily_price_info.highest_price is '고가';
comment on column products_daily_price_info.lowest_price is '저가';
comment on column products_daily_price_info.close_price is '종가';
comment on column products_daily_price_info.trading_quantity is '거래량';
comment on column products_daily_price_info.trading_price is '거래대금';
comment on column products_daily_price_info.shares_outstanding is '상장주식수';
comment on column products_daily_price_info.mrkt_total_amount is '시가총액. 종가 * 상장좌수';
comment on column products_daily_price_info.created_at is '레코드 생성 시간';
comment on column products_daily_price_info.modified_at is '레코드 수정 시간';

comment on table etfs_daily_price_info is 'ETF시세정보';
comment on column etfs_daily_price_info.isin_cd is 'ISIN_국제_코드';
comment on column etfs_daily_price_info.base_date is '기준일';
comment on column etfs_daily_price_info.total_net_assets is 'ETF 순자산총액';
comment on column etfs_daily_price_info.nav is '순자산가치(순자산총액 / 상장좌수)';
comment on column etfs_daily_price_info.created_at is '레코드 생성 시간';
comment on column etfs_daily_price_info.modified_at is '레코드 수정 시간';

comment on table products_distribution_info is '분배/배당금 정보';
comment on column products_distribution_info.isin_cd is 'ISIN_국제_코드';
comment on column products_distribution_info.base_date is '지급기준일';
comment on column products_distribution_info.payable_date is '실지급일';
comment on column products_distribution_info.cash_per_share is '주당분배금';
comment on column products_distribution_info.payout_yield_pct is '시가대비 분배율';
comment on column products_distribution_info.tax_basis is '결산과표기준';
comment on column products_distribution_info.distribution_type is '배당구분';
comment on column products_distribution_info.created_at is '레코드 생성 시간';
comment on column products_distribution_info.modified_at is '레코드 수정 시간';