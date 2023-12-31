package com.pofol.main.orders.payment.service;

import com.pofol.main.member.dto.GradeDto;
import com.pofol.main.member.dto.MemberDto;
import com.pofol.main.member.repository.GradeRepository;
import com.pofol.main.member.repository.MemberRepository;
import com.pofol.main.orders.order.domain.OrderCheckout;
import com.pofol.main.orders.order.domain.OrderDto;
import com.pofol.main.orders.order.repository.OrderRepository;
import com.pofol.main.orders.payment.domain.PaymentDto;
import com.pofol.main.orders.payment.domain.PaymentHistoryDto;
import com.pofol.main.orders.payment.repository.PaymentHistoryRepository;
import com.pofol.main.orders.payment.repository.PaymentRepository;
import com.pofol.main.product.cart.SelectedItemsDto;
import com.pofol.main.product.cart.CartRepository;
import com.pofol.main.product.domain.OptionProductDto;
import com.pofol.main.product.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final GradeRepository gradeRepository;
    private final CartRepository basketRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;


    @Override
    @Transactional
    public Boolean prevVerify(OrderCheckout oc) {
        List<SelectedItemsDto> items = oc.getSelectedItems();
        int jsTotPayPrice = oc.getTot_pay_price(); //js에서 넘어온 실 결제 금액
        int dbTotPayPrice = 0; //

        try {
            for (SelectedItemsDto item : items) {
                String opt_prod_id = item.getOpt_prod_id();
                Integer qty = item.getQty();

                if (opt_prod_id == null || opt_prod_id.isEmpty()) { //일반 상품일 때
                    ProductDto prod = basketRepository.selectProduct(item.getProd_id());
                    dbTotPayPrice += prod.getDisc_price() * qty;
                } else { //옵션 상품일 때
                    OptionProductDto prod = basketRepository.selectOptionProduct(item.getOpt_prod_id());
                    dbTotPayPrice += prod.getOpt_disc_price() * qty;
                }
            }

            if (dbTotPayPrice < 40000) { //배송비 추가
                dbTotPayPrice += 3000;
            }

            dbTotPayPrice -= (oc.getCoupon_disc() + oc.getPoint_used());

            System.out.println("jsTotPayPrice = " + jsTotPayPrice);
            System.out.println("dbTotPayPrice = " + dbTotPayPrice);
            return jsTotPayPrice == dbTotPayPrice; //같으면 true, 다르면 false
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public PaymentDto writePayment(PaymentDto pay) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mem_id = authentication.getName(); //회원id

        try {
            //결제 table 작성
            pay.setMemberData(mem_id);
            //적립금 계산 <- 결제 완료 시에만 필요
            if(pay.getSuccess().equals("true")){
                GradeDto grade = gradeRepository.select_grade(mem_id);
                System.out.println(grade);
                pay.setReserves(pay.getTot_pay_price() * grade.getAcm_rate() / 100);
            }
            paymentRepository.insert(pay);

            //결제이력 table 작성
            PaymentHistoryDto paymentHistoryDto = new PaymentHistoryDto(pay.getPay_id(), pay.getOrd_id(), pay.getMem_id(), pay.getCode_name(), pay.getPay_way(), pay.getTot_prod_name(), pay.getTot_pay_price(), pay.getPg_tid(), pay.getMem_id(), pay.getMem_id());
            paymentHistoryRepository.insert(paymentHistoryDto);
            return pay;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public Boolean nextVerify(PaymentDto pd) {
        int jsTotPayPrice = pd.getTot_pay_price(); //js에서 넘어온 실 결제 금액
        int dbTotPayPrice = 0;

        try {
            OrderDto orderDto = orderRepository.select(pd.getOrd_id());
            dbTotPayPrice = orderDto.getTot_pay_price(); //주문table에서 실 결제 금액 가져옴(1차 검증 성공 후 db에 저장한 것)

            System.out.println("2차 검증: "+(jsTotPayPrice==dbTotPayPrice));
            return jsTotPayPrice == dbTotPayPrice; //같은면 true, 다르면 false
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public PaymentDto getPayment(Long ord_id) {
        try {
            PaymentDto paymentDto = paymentRepository.select(ord_id);
            return paymentDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
