package com.glenn.myblog.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("/writing으로 GET 요청시 article-edit이라는 View Name을 반환")
    @Test
    public void moveToWritingPage() throws Exception {
        this.mockMvc.perform(get("/writing"))
                .andExpect(status().isOk())
                .andExpect(view().name("article-edit"));
    }
}
