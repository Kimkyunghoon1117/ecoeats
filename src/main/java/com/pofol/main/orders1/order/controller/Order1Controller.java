package com.pofol.main.orders1.order.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class Order1Controller {
	@GetMapping("/order")
    public String order(Model m, HttpServletRequest request){
		System.out.println("OrderController.order()");
		
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "/order/mypageOrder";
    }
}