package com.pofol.admin.product;

import com.pofol.main.orders1.order.domain.CodeTableDto;
import com.pofol.main.product.domain.ProductDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductAdminRepositoryImpl implements ProductAdminRepository{

    private final SqlSession sqlSession;

    private final String namespace = "com.pofol.admin.product.ProductAdminRepository.";

    @Override
    public List<CodeTableDto> selectCodeType(Integer code_type) throws Exception {
        return null;
    }

    @Override
    public List<ProductDto> selectAll() throws Exception {
        return sqlSession.selectList(namespace + "selectAll");
    }

    @Override
    public int update(ProductDto productDto) throws Exception {
        return 0;
    }

    @Override
    public int count() throws Exception {
        return 0;
    }

    @Override // 조건에 따른 상품 리스트 정렬 (관리자)
    public List<ProductDto> searchSelectPage(SearchProductAdminCondition searchProductAdminCondition, String selling, String display) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword_type", searchProductAdminCondition.getKeyword_type());
        map.put("keyword", searchProductAdminCondition.getKeyword());
        map.put("offset", searchProductAdminCondition.getOffset());
        map.put("pageSize", searchProductAdminCondition.getPageSize());
        map.put("selling", selling);
        map.put("display", display);
        return sqlSession.selectList(namespace + "searchSelectPage", map);
    }

    @Override // 조건에 따른 상품 리스트 카운트 (관리자)
    public Integer searchResultCnt(SearchProductAdminCondition searchProductAdminCondition) throws Exception {
        return sqlSession.selectOne(namespace + "searchResultCnt", searchProductAdminCondition);
    }
}