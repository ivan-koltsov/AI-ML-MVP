package com.aiml.fintech.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Contract tests for auth API. Requires MongoDB running on localhost:27017.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthApiContractTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signUp_validPayload_returns201AndUserResponse() throws Exception {
        String email = "contract-" + System.currentTimeMillis() + "@test.com";
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("email", email, "password", "Test1234"));

        mvc.perform(post("/api/v1/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void signUp_duplicateEmail_returns400() throws Exception {
        String email = "dup-" + System.currentTimeMillis() + "@test.com";
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("email", email, "password", "Test1234"));

        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void signIn_validCredentials_returns200() throws Exception {
        String email = "signin-" + System.currentTimeMillis() + "@test.com";
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("email", email, "password", "Test1234"));

        mvc.perform(post("/api/v1/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/auth/sign-in").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void signIn_invalidCredentials_returns401() throws Exception {
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("email", "nonexistent@test.com", "password", "WrongPass1"));

        mvc.perform(post("/api/v1/auth/sign-in").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getMe_unauthenticated_returns401() throws Exception {
        mvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void signOut_unauthenticated_returns401() throws Exception {
        mvc.perform(post("/api/v1/auth/sign-out"))
                .andExpect(status().isUnauthorized());
    }
}
