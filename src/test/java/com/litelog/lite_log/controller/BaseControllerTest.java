package com.litelog.lite_log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions performPostRequest(String url, Object request, HttpStatus expectedStatus,
                                               String expectedMessage) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(jsonPath("$.status").value(expectedStatus.value()));

        if (expectedMessage != null) {
            resultActions.andExpect(jsonPath("$.message").value(expectedMessage));
        }

        return resultActions;
    }

    protected ResultActions performGetRequest(String url, Map<String, String> params, HttpStatus expectedStatus, String expectedMessage)
            throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (params != null) {
            params.forEach(requestBuilder::param);
        }

        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(expectedStatus.value()))
                .andExpect(jsonPath("$.status").value(expectedStatus.value()));

        if (expectedMessage != null) {
            resultActions.andExpect(jsonPath("$.message").value(expectedMessage));
        }

        return resultActions;
    }
}
