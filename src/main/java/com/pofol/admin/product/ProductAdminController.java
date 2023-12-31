package com.pofol.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pofol.main.product.category.CategoryDto;
import com.pofol.main.product.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    @GetMapping("/dashboard")
    public String goDashboard() {
        return "/admin/dashboard/dashboard";
    }

    @GetMapping("/product/list")
    public String getAdminProductList(SearchProductAdminCondition sc,
                                      Model model, ProductFilterDto productFilterDto, String Stock) {

        try {
            // 카테고리 정렬
            ObjectMapper objectMapper = new ObjectMapper();
            List<CategoryDto> list = productAdminService.getCategoryList();
            String categoryList = objectMapper.writeValueAsString(list);
            model.addAttribute("categoryList", categoryList);

            // 페이징
            Integer totalCount = productAdminService.getProductAdminSearchCount(sc, productFilterDto, Stock);
            PageHandler pageHandler = new PageHandler(totalCount, sc);
            model.addAttribute("ph", pageHandler);
            model.addAttribute("totalCount", totalCount);

            // 상품 관리자 페이지에서 조건에 따라 상품조회
            List<ProductDto> productAdminList = productAdminService.getProductAdminSearchList(sc, productFilterDto, Stock);
            model.addAttribute("product", productAdminList);
            model.addAttribute("category", list);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/"; // 일단 index로 보냄 (관리자 홈으로 나중에 변경할 것)
        }

        return "admin/product/productAdminList";
    }

    // 관리자 페이지에서 상품상태 수정 + 이력 insert
    @PostMapping("product/modify")
    public String AdminProductModify(Long[] selectProductId, ProductStatusUpdate productStatusUpdate) {

        // 판매가 변경 + 판매기간 변경 (추가해야함)
        try {
            List<ProductDto> severalProduct = productAdminService.getSeveralProduct(selectProductId);
            productAdminService.updateProductStatus(severalProduct, productStatusUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/product/list";
        }
            return "redirect:/admin/product/list"; // (변경해야함)
    }
    
    // 상품 그룹 관리 + main 페이지 그룹별 진열 관리
}
