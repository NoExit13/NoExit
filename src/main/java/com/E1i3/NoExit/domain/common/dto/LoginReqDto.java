package com.E1i3.NoExit.domain.common.dto;

import com.E1i3.NoExit.domain.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginReqDto {
	private String email;
	private String password;
	private Role role;
}
