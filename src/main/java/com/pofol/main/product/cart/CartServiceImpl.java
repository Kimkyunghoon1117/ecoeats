package com.pofol.main.product.cart;

import com.pofol.main.orders.order.domain.ProductOrderCheckout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    @Override // 장바구니에 상품을 저장
    public int saveCartProduct(CartDto cartDto) throws Exception {
        return cartRepository.insertCartProduct(cartDto);
    }

    @Override // 장바구니 페이지에 상품 리스트 정렬
    public List<CartDto> getCartProductList(String mem_id) throws Exception {
        return cartRepository.selectAllCartProduct(mem_id);
    }

    @Override // 장바구니 상품 주문서로 넘기기
    public ProductOrderCheckout getCartProductOrderCheck(SelectedItemsDto selectedItemsDto) throws Exception {
        return cartRepository.selectProductOrderCheckout(selectedItemsDto);
    }
}