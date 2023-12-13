package com.pofol.main.product.controller;

import com.pofol.main.product.domain.ProductDto;
import com.pofol.main.product.service.ProductService;
import com.pofol.util.AwsS3ImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/image")
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final ProductService productService;
    private final AwsS3ImgUploaderService awsS3ImgUploaderService;

    @PostMapping("/upload")
    public String imageUploadPOST(ProductDto productDto, RedirectAttributes redirectAttributes) throws Exception {
        log.info("--------------imageUploadPOST----------------");
        log.info("productDto : " + productDto);
        log.info("productDto.getProd_img() : " + productDto.getProd_img());
        log.info("productDto.getProd_img().getOriginalFilename() : " + productDto.getProd_img().getOriginalFilename());
        String imgUrl = awsS3ImgUploaderService.uploadImageToS3(
                productDto.getProd_img(), "product");
        //productService.productEnroll(productDto);
        return "redirect:/prodEnroll";
    }

}