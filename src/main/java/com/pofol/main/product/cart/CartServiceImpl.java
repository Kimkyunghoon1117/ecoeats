package com.pofol.main.product.cart;

import com.pofol.main.orders.order.domain.ProductOrderCheckout;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public List<CartDto> goCartProductCount(List<CartDto> cartDtoList) throws Exception {

        for (CartDto cartDto : cartDtoList) {
            Integer discPrice = cartDto.getDisc_price();
            Integer optDiscPrice = cartDto.getOpt_disc_price();
            Integer qty = cartDto.getQty();
            if (cartDtoList.size() == 1) {
                cartDto.setTotal_price(discPrice * qty);
            } else {
                cartDto.setTotal_price(optDiscPrice * qty);
            }
        }

        return cartDtoList;
    }

    @Transactional
    @Override // 장바구니에 상품을 저장
    public int saveCartProduct(List<CartDto> cartDtoList) throws Exception {

        int totalChangeCount = 0;

        for (CartDto cartDto : cartDtoList) {

            if (cartDto.getQty() != 0) {
                String optProdId = cartDto.getOpt_prod_id();
                Long prodId = cartDto.getProd_id();
                Integer qty = cartDto.getQty();
                boolean newProduct = true;

                List<CartDto> cartProduct = cartRepository.selectAllCartProduct(cartDto.getMem_id());

                for (CartDto cp : cartProduct) {

                    //update(이미 담긴 상품을 또 담을 시 수량 증가)
                    if (optProdId != null && cp.getOpt_prod_id() != null && cp.getOpt_prod_id().equals(optProdId)) {
                        cartDto.setQty(cp.getQty() + qty);
                        cartRepository.updateCartProduct(cartDto);

                        newProduct = false;
                        totalChangeCount++;
                    } else if (optProdId == null && cp.getProd_id().equals(prodId)) {
                        cartDto.setQty(cp.getQty() + qty);
                        cartRepository.updateCartProduct(cartDto);

                        newProduct = false;
                        totalChangeCount++;
                    }
                }
                if (newProduct) {
                    cartRepository.insertCartProduct(cartDto);
                    totalChangeCount++;
                }
            }
        }
        return totalChangeCount;
    }

    @Override // 장바구니 페이지에 상품 리스트 정렬
    public List<CartDto> getCartProductList(String mem_id) throws Exception {

        List<CartDto> cartProduct = cartRepository.selectAllCartProduct(mem_id);

        for (CartDto cartDto : cartProduct) {

            Integer prodPrice = cartDto.getProd_price();
            Integer prodDiscPrice = cartDto.getDisc_price();
            Integer optPrice = cartDto.getOpt_price();
            Integer optDiscPrice = cartDto.getOpt_disc_price();
            Integer qty = cartDto.getQty();

            if (optPrice == null) {
                cartDto.setTotal_price(prodPrice * qty);
                cartDto.setTotal_disc_price(prodDiscPrice * qty);
            } else {
                cartDto.setTotal_price(optPrice * qty);
                cartDto.setTotal_disc_price(optDiscPrice * qty);
            }
        }
        return cartProduct;
    }

    @Override // 장바구니 상품 삭제
    public int removeCartProduct(CartDto cartDto) throws Exception {
        // 회원 아이디 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberID = authentication.getName();
        cartDto.setMem_id(memberID);

        return cartRepository.deleteCartProduct(cartDto);
    }

    @Override // 장바구니 상품 수량 계산
    public CartDto getCartProductPrice(CartDto cartDto) throws Exception {

        // 회원 아이디 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberID = authentication.getName();
        cartDto.setMem_id(memberID);

        Integer prodPrice = cartDto.getProd_price();
        Integer prodDiscPrice = cartDto.getDisc_price();
        Integer optPrice = cartDto.getOpt_price();
        Integer optDiscPrice = cartDto.getOpt_disc_price();
        Integer qty = cartDto.getQty();

        if (prodPrice != null && prodDiscPrice != null) {

            cartDto.setTotal_price(prodPrice * qty);
            cartDto.setTotal_disc_price(prodDiscPrice * qty);

        } else if (optPrice != null && optDiscPrice != null) {

            cartDto.setTotal_price(optPrice * qty);
            cartDto.setTotal_disc_price(optDiscPrice * qty);
        }

        // 장바구니 상품 수량변경 데이터에 저장
        try {
            cartRepository.updateCartProduct(cartDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartDto;
    }

    @Override // 장바구니 상품 주문서로 넘기기
    public ProductOrderCheckout getCartProductOrderCheck(SelectedItemsDto selectedItemsDto) throws Exception {
        return cartRepository.selectProductOrderCheckout(selectedItemsDto);
    }
}
