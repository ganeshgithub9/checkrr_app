package com.example.checkrr.entity;

import com.example.checkrr.enums.AdverseActionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adverse_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdverseAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate preNoticeDate;


    private LocalDate postNoticeDate;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private AdverseActionStatus status;

    private String mailSubject;

    private String mailContentInHtml;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @ElementCollection
    @CollectionTable(name="attachment",joinColumns = @JoinColumn(name = "adverse_action_id"))
    @Column(name = "attachment_url")
    List<String> attachments=new ArrayList<>();

    public AdverseAction(Long id,LocalDate preNoticeDate,LocalDate postNoticeDate,Candidate candidate,AdverseActionStatus status){
        this.id=id;
        this.preNoticeDate=preNoticeDate;
        this.postNoticeDate=postNoticeDate;
        this.candidate=candidate;
        this.status=status;
    }
}

