package com.E1i3.NoExit.domain.comment.repository;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAll(Pageable pageable);
    Page<Comment> findByBoardAndDelYN(Pageable pageable, Board board, DelYN delYN);
    Page<Comment> findByDelYN(Pageable pageable, DelYN delYN);
}