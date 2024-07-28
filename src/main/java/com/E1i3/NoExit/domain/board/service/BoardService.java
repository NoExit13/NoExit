package com.E1i3.NoExit.domain.board.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.domain.DelYN;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }


    public void boardCreate(BoardCreateReqDto dto) { // 게시글 생성

        // Member 엔티티를 memberId로 조회
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + dto.getMemberId()));

        // DTO를 Entity로 변환하고 Member 설정
        Board board = dto.toEntity(member);
        boardRepository.save(board);
    }


    public Page<BoardListResDto> boardList(Pageable pageable) { // 게시글 전체 조회
        Page<Board> boards = boardRepository.findByDelYN(pageable,"N");
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardListResDto> boardListResDtos = boards.map(Board::fromEntity);
        return boardListResDtos;
    }

    public BoardDetailResDto boardDetail(Long id) { // 특정 게시글 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        if (board.getDelYN().equals(DelYN.Y)) {
            throw new IllegalArgumentException("cannot find board");
        }
        board.updateBoardHits(); // 조회수 +1
        return board.detailFromEntity();
    }


    public Board boardUpdate(Long id, BoardUpdateReqDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));

        if (!board.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다.");
        }

        board.updateEntity(dto);
        return boardRepository.save(board);
    }

    public void boardDelete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
//        boardRepository.delete(board);
        if (board.getDelYN().equals(DelYN.Y)) {
            throw new IllegalArgumentException("cannot find board");
        }
        board.deleteEntity();
        boardRepository.save(board);
    }

    public int boardUpdateLikes(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        board.updateLikes();
        boardRepository.save(board);
        return board.getLikes();
    }

    public int boardUpdateDislikes(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        board.updateDislikes();
        boardRepository.save(board);
        return board.getDislikes();
    }
}
