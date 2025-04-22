package com.demo.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavouriteResponse {

    private Long id;
    private Integer memberId;
    private Integer attractionId;
    private Date createdAt;
}
