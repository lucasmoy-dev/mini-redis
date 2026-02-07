package com.miniredis.infrastructure.adapters.in.rest;

import com.miniredis.domain.ports.in.RedisPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.miniredis.domain.constant.RedisConstants.OK;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedisController.class)
class RedisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisPort redis;

    @Test
    void shouldExecuteCommandViaQueryParam() throws Exception {
        when(redis.executeCommand("SET mykey val")).thenReturn(OK);

        mockMvc.perform(get("/api").param("cmd", "SET mykey val"))
                .andExpect(status().isOk())
                .andExpect(content().string(OK));
    }

    @Test
    void shouldGetKeyViaRest() throws Exception {
        when(redis.get("mykey")).thenReturn("myval");

        mockMvc.perform(get("/api/mykey"))
                .andExpect(status().isOk())
                .andExpect(content().string("myval"));
    }

    @Test
    void shouldSetKeyViaRest() throws Exception {
        when(redis.set("mykey", "myval")).thenReturn(OK);

        mockMvc.perform(put("/api/mykey").content("myval"))
                .andExpect(status().isOk())
                .andExpect(content().string(OK));
    }

    @Test
    void shouldDeleteKeyViaRest() throws Exception {
        when(redis.delete("mykey")).thenReturn(1L);

        mockMvc.perform(delete("/api/mykey"))
                .andExpect(status().isOk())
                .andExpect(content().string(OK));
    }

    @Test
    void shouldReturnNilWhenKeyDoesNotExist() throws Exception {
        when(redis.get("nonexistent")).thenReturn("(nil)");

        mockMvc.perform(get("/api/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().string("(nil)"));
    }
}
