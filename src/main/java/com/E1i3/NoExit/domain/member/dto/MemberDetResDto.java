package com.E1i3.NoExit.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetResDto {
	// 마이페이지
	private String username;
	private String password;
	private String email;
	private String phone_number;
	private String nickname;
	private String profile_image;
	private int age;
}
