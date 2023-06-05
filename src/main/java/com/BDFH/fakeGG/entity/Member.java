package com.BDFH.fakeGG.entity;

import com.BDFH.fakeGG.auth.MemberDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "member_email", nullable = false, unique = true)
    private String memberEmail;

    @Column(nullable = false, unique = true)
    private String memberName;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private Long memberBirth;




}
