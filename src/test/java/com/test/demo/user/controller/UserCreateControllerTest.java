package com.test.demo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.user.domain.dto.request.UserCreateDto;
import com.test.demo.user.domain.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.anyOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserCreateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JavaMailSender javaMailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_회원가입을_할_수_있고_회원가입_후_PENDING_상태이다() throws Exception {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("tester@test.com")
                .nickname("tester")
                .address("Seoul")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // when
        // then
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("tester@test.com"))
                .andExpect(jsonPath("$.nickname").value("tester"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value(UserStatus.PENDING));
    }
}
