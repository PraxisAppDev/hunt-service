package com.afterhoursdev.scavengerhunt.hunt.dto.response;

import java.io.Serializable;
import java.util.List;

import com.afterhoursdev.scavengerhunt.hunt.domain.HuntHistory;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
public class HuntHistoryResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private List<HuntHistory> hunts;
}