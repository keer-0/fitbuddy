package com.laszlojanku.fitbuddy.jpa.service.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.laszlojanku.fitbuddy.dto.AppUserDto;
import com.laszlojanku.fitbuddy.jpa.entity.AppUser;
import com.laszlojanku.fitbuddy.jpa.repository.AppUserCrudRepository;
import com.laszlojanku.fitbuddy.jpa.service.converter.AppUserConverterService;
import com.laszlojanku.fitbuddy.testhelper.AppUserTestHelper;
import com.laszlojanku.fitbuddy.testhelper.RoleTestHelper;

@ExtendWith(MockitoExtension.class)
class AppUserCrudServiceTest {
	
	@InjectMocks	AppUserCrudService instance;
	@Mock	AppUserCrudRepository appUserCrudRepository;
	@Mock	AppUserConverterService appUserConverterService;
	
	@Test
	void readByName_whenNameIsNull_shouldReturnNull() {
		AppUserDto actualAppUserDto = instance.readByName(null);
		
		assertNull(actualAppUserDto);		
	}
	
	@Test
	void readByName_whenAppUserNotFound_shouldReturnNull() {
		when(appUserCrudRepository.findByName(anyString())).thenReturn(Optional.empty());
		
		AppUserDto actualAppUserDto = instance.readByName("name");
		
		assertNull(actualAppUserDto);
	}
	
	@Test
	void readByName_whenAppUserFound_shouldReturnAppUserDto() {
		AppUser appUserMock = AppUserTestHelper.getMockAppUser(1, "name", "password");
		AppUserDto appUserDtoMock = new AppUserDto(1, "name", "password", "roleName");
		
		when(appUserCrudRepository.findByName(anyString())).thenReturn(Optional.of(appUserMock));
		when(appUserConverterService.convertToDto(any(AppUser.class))).thenReturn(appUserDtoMock);
		
		AppUserDto actualAppUserDto = instance.readByName("name");
		
		assertEquals(appUserDtoMock, actualAppUserDto);
	}	
	
	@Test
	void update_whenIdIsNull_shouldReturnNull() {
		AppUserDto actualAppUserDto = instance.update(null, new AppUserDto(1, "name", "password", "roleName"));
		
		assertNull(actualAppUserDto);
	}
	
	@Test
	void update_whenAppUserDtoIsNull_shouldReturnNull() {
		AppUserDto actualAppUserDto = instance.update(1, null);
		
		assertNull(actualAppUserDto);
	}
	
	@Test
	void update_whenExistingAppUserNotFound_shouldReturnNull() {
		when(appUserCrudRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		AppUserDto actualAppUserDto = instance.update(1, new AppUserDto(1, "name", "password", "roleName"));
		
		assertNull(actualAppUserDto);
	}
	
	@Test
	void update_whenExistingAppUserFound_shouldReturnUpdatedAppUserDto() {
		AppUser appUserMock = AppUserTestHelper.getMockAppUser(1, "name", "password", RoleTestHelper.getMockRole(1, "roleName"));		
		AppUserDto appUserDtoMock = new AppUserDto(1, "name", "password", "roleName");
		
		when(appUserCrudRepository.findById(anyInt())).thenReturn(Optional.of(appUserMock));
		when(appUserConverterService.convertToDto(any(AppUser.class))).thenReturn(appUserDtoMock);
		when(appUserConverterService.convertToEntity(any(AppUserDto.class))).thenReturn(appUserMock);
		
		AppUserDto actualAppUserDto = instance.update(1, new AppUserDto(1, "newName", "newPassword", "newRoleName"));
		
		verify(appUserCrudRepository).save(appUserMock);		
		assertEquals(1, actualAppUserDto.getId());
		assertEquals("newName", actualAppUserDto.getName());
		assertEquals("newPassword", actualAppUserDto.getPassword());
		assertEquals("newRoleName", actualAppUserDto.getRolename());
	}	

}
