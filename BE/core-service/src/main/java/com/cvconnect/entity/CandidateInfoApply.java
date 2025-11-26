package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "candidate_info_apply")
public class CandidateInfoApply extends BaseEntity {
    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Size(max = 255)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @NotNull
    @Column(name = "cv_file_id", nullable = false)
    private Long cvFileId;

    @Column(name = "cover_letter", length = Integer.MAX_VALUE)
    private String coverLetter;

}