package com.kmii.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
   
	@RequestMapping(value = {"/{path:^(?!api$|static|index\\.html$).*$}/**" })
    public String forward() {
        return "forward:/index.html";
    }
   //api로 들어오는 요청 -> 스프링 부트 요청으로 적용
   //api 요청을 제외한 요청 - React 라우터 요청으로 변경  -> 리액트의 "/"로 시작하는 요청으로 변경

}
