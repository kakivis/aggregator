package com.api.aggregator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HerokuContactDto {
  private Integer id;
  private String name;
  private String email;
  private String created_at;
  private String updated_at;
}
