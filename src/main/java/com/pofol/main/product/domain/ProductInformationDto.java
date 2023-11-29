package com.pofol.main.product.domain;

import lombok.Getter;

@Getter
public class ProductInformationDto { // 상품 정보

    private int prod_id; // 상품 id
    private String origin; // 원산지
    private String weight; // 중량/용량
    private String brand; // 브랜드
    private String seller; // 판매자
    private String exp_date; // 유통기한
    private String as_guide; // 안내사항
    private String sales_unit; // 판매 단위
    private String pack_type; // 포장 타입

    public ProductInformationDto() {}

    public ProductInformationDto(int prod_id, String origin, String weight, String brand, String seller, String exp_date, String as_guide, String sales_unit, String pack_type) {
        this.prod_id = prod_id;
        this.origin = origin;
        this.weight = weight;
        this.brand = brand;
        this.seller = seller;
        this.exp_date = exp_date;
        this.as_guide = as_guide;
        this.sales_unit = sales_unit;
        this.pack_type = pack_type;
    }

}