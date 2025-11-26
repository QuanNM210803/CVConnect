package com.cvconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nmquan.commonlib.model.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "search_history_outside")
public class SearchHistoryOutside extends BaseEntity {
    @Size(max = 255)
    @Column(name = "keyword")
    private String keyword;

    @Column(name = "user_id")
    private Long userId;

}