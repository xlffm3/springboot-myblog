package com.glenn.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/writing")
    public String moveToWritingPage() {
        return "article-edit";
    }
}

/*
게시글에 필요한 스펙

id authorName content createdDate modifiedDate
 */
