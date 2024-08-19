package com.E1i3.NoExit.domain.wishlist.service;

import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.wishlist.dto.WishReqDto;
import com.E1i3.NoExit.domain.wishlist.dto.WishResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.game.repository.GameRepository;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;
import com.E1i3.NoExit.domain.wishlist.repository.WishListRepository;

@Service
@Transactional
public class WishListService {

	private final WishListRepository wishListRepository;
	private final MemberRepository memberRepository;
	private final GameRepository gameRepository;

	public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository,
		GameRepository gameRepository) {
		this.wishListRepository = wishListRepository;
		this.memberRepository = memberRepository;
		this.gameRepository = gameRepository;
	}


	public WishList addWishList(WishReqDto dto) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));

		WishList wishList = dto.toEntity(member);
		// 위시리스트 추가시 카운트 추가
		Game game = gameRepository.findById(dto.getGameId()).orElseThrow(() -> new EntityNotFoundException("일치하는 게임 정보가 존재하지 않습니다."));
		game.addToWishList();
		return wishListRepository.save(wishList);
	}


//	public List<GameResDto> getWishList() {
//		String email = SecurityContextHolder.getContext().getAuthentication().getName();
//		Member member = memberRepository.findByEmail(email)
//			.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
//		List<WishList> wishLists = wishListRepository.findByMemberAndDelYN(member, DelYN.N);
//
//		return wishLists.stream()
//				.map(wishList -> gameRepository.findById(wishList.getGameId())
//						.orElseThrow(() -> new EntityNotFoundException("일치하는 정보가 존재하지 않습니다.")))
//				.map(Game::fromEntity)
//				.collect(Collectors.toList());
//	}


	public Page<WishResDto> getWishList(Pageable pageable) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
		Page<WishList> wishLists = wishListRepository.findByMemberAndDelYN(pageable, member, DelYN.N);
		Page<WishResDto> dtos = wishLists.map(WishList::fromEntity);


		return dtos;
	}





	public WishList deleteWishList(Long gameId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
		WishList wishList = wishListRepository.findById(gameId).orElseThrow(()->new EntityNotFoundException("일치하는 정보가 존재하지 않습니다."));
		wishList.deleteEntity();
		return wishListRepository.save(wishList);
	}
}