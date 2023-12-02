package com.pofol.main.member.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println(savedRequest);

        Enumeration<String> list = request.getSession().getAttributeNames();
        while(list.hasMoreElements()){
            String a= list.nextElement();
            System.out.println("세션 Attribute 확인 : "+a+" : "+ request.getSession().getAttribute(a));

        }

        String referer = request.getHeader("Referer");
        System.out.println("referer : "+ referer);


        HttpSession session = request.getSession();
        session.setAttribute("greeting", authentication.getName() + "님 반갑습니다.");


        response.sendRedirect(savedRequest.getRedirectUrl());

    }


}



//    // IP, 세션 ID
//    WebAuthenticationDetails web = (WebAuthenticationDetails) authentication.getDetails();
//        System.out.println("IP : " + web.getRemoteAddress());
//                System.out.println("Session ID : " + web.getSessionId());
//
//                // 인증 ID
//                System.out.println("name : " + authentication.getName());
//
//                // 권한 리스트
//                List<GrantedAuthority> authList = (List<GrantedAuthority>) authentication.getAuthorities();
//        System.out.print("권한 : ");
//        for (int i = 0; i < authList.size(); i++) {
//        System.out.print(authList.get(i).getAuthority() + " ");
//        }
//        System.out.println();