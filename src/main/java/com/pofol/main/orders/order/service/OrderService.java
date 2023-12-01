package com.pofol.main.orders.order.service;

import com.pofol.main.orders.order.domain.OrderDto;
import com.pofol.main.orders.sample.cartDataSample.SelectedItemsDto;

import java.util.List;

public interface OrderService {

    //장바구니에서 넘어온 데이터를 가지고 실 주문 금액과 실 결제 금액 계산
    OrderDto calculateProductInfo(List<SelectedItemsDto> items);

    OrderDto getOrder();
}