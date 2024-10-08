package com.E1i3.NoExit.domain.comment.domain;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
//    private Long boardId; // 댓글 단 게시글 아이디
    private Board board;

//    private Long memberId; // 댓글 작성자 아이디
//    private String writer; // 댓글 작성자 닉네임
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 댓글 작성자

    private String contents; // 댓글 내용

    private int likes; // 좋아요
    private int dislikes; // 싫어요


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN = DelYN.N; // 삭제 여부


    public CommentListResDto fromEntity(){
        String createdTime
                = this.getCreatedTime().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        CommentListResDto commentListResDto = CommentListResDto.builder()
                .id(this.id)
                .writer(this.member.getNickname())
                .contents(this.contents)
                .likes(this.likes)
                .dislikes(this.dislikes)
                .createdTime(createdTime)
                .build();

        return commentListResDto;
    }

    public void updateEntity(CommentUpdateReqDto dto) {
        this.contents = dto.getContents();
    }

    public void deleteEntity() {
        this.delYN = DelYN.Y;
    }

    public void updateLikes(boolean like) {
        if(like) {
            this.likes++;
        }else{
            this.likes--;
        }
    }

    public void updateDislikes(boolean dislike) {
        if(dislike) {
            this.dislikes++;
        }else{
            this.dislikes--;
        }
    }
}
