package com.E1i3.NoExit.domain.reservation.dto;


import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.reservation.domain.ApprovalStatus;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.reservation.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSaveDto {
    private String resName;
    private String phoneNumber;
    private int numberOfPlayers;
    private LocalDate resDate;
    private String resDateTime;
    private String email;
    private Long GameId; // 게임 아디 검증
    private Long ownerId;

    public Reservation toEntity(Member member, Game game, Owner owner) {
        return Reservation.builder()
                .member(member)
                .game(game)
                .owner(owner)
                .resName(this.resName)
                .phoneNumber(this.phoneNumber)
                .numberOfPlayers(this.numberOfPlayers)
                .resDate(this.resDate)
                .resDateTime(this.resDateTime)
                .reservationStatus(ReservationStatus.WAITING)
                .approvalStatus(ApprovalStatus.READY)
                .createdTime(LocalDateTime.now())
                .delYN(DelYN.N)
                .build();
    }
}
