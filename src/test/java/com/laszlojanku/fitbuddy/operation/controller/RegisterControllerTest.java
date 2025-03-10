package com.laszlojanku.fitbuddy.operation.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laszlojanku.fitbuddy.FitBuddyApplication;
import com.laszlojanku.fitbuddy.config.SecurityConfig;
import com.laszlojanku.fitbuddy.dto.RegisterDto;
import com.laszlojanku.fitbuddy.operation.service.NewUserService;
import com.laszlojanku.fitbuddy.operation.service.RegisterService;

@WebMvcTest(RegisterController.class)
@ContextConfiguration(classes = {FitBuddyApplication.class, SecurityConfig.class})
public class RegisterControllerTest {
	
	@Autowired	MockMvc mockMvc;
	@Autowired	ObjectMapper objectMapper;
	@MockBean	RegisterService registerService;
	@MockBean	NewUserService newUserService;
	
	@Test
	public void register_whenInputIsCorrect_shouldReturnOk() throws Exception {
		RegisterDto registerDtoMock = new RegisterDto("name", "password");
		
		when(registerService.register(anyString(), anyString())).thenReturn(1);
		
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDtoMock)))		
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(registerService).register(anyString(), anyString());
		verify(newUserService).addDefaultExercises(anyInt());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"nam", "namenamenamename"}) // 3 and 16 characters
	public void register_whenNameSizeNotCorrect_shouldReturnBadRequest(String name) throws Exception {
		RegisterDto registerDtoMock = new RegisterDto(name, "password");
		
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDtoMock)))		
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"pas", "passwordpassword"}) // 3 and 16 characters
	public void register_whenPasswordSizeNotCorrect_shouldReturnBadRequest(String password) throws Exception {
		RegisterDto registerDtoMock = new RegisterDto("name", password);
		
		mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDtoMock)))		
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
