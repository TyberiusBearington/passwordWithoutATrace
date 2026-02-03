package com.pwat.passwordwithoutatrace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import com.pwat.passwordwithoutatrace.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PasswordController.class)
public class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordService passwordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePassword() throws Exception {
        when(passwordService.createPassword(any(), anyInt(), anyLong())).thenReturn("test-id");

        CreatePasswordRequest request = new CreatePasswordRequest();
        request.setPassword("password");
        request.setViews(1);
        request.setExpirationTime(60);

        mockMvc.perform(post("/passwords")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("test-id"));
    }

    @Test
    public void testGetPassword() throws Exception {
        PasswordEntry entry = new PasswordEntry("password", 1);
        when(passwordService.getPassword("test-id")).thenReturn(entry);

        mockMvc.perform(get("/passwords/test-id"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(entry)));
    }
}
