package com.afterhoursdev.scavengerhunt.hunt.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Hunt class stores basic information about a hunt. Instances
 * of this class are used to represent individual hunts.
 *    
 * @author  Jim Zombek
 * @version 1.0
 * @since   07-2-2024
*/

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter
@Document(collection = "hunts")
public class Hunt {
  @Id
  private String id;
  private String name;
  private String description;
  private String venue;
  private String address;
  private String city;
  private String stateAbbr;
  private String zipcode;
  private String logoURL;
  private String startDate;
  private String endDate;
  private int    teamLimit;
}