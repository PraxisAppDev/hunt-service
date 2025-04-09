package com.afterhoursdev.scavengerhunt.hunt.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * The HuntHistory class stores basic information about a hunt's
 * history. Instances of this class are used to represent an 
 * individual hunt history.
 *     
 * @author  Jim Zombek
 * @version 1.0
 * @since   07-2-2024
*/

@Getter  
@Setter
public class HuntHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String venue;
	private String logoURL;
	private String startDate;
	private int    place;
}