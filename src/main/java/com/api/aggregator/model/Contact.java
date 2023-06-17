package com.api.aggregator.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Contact {
  private Integer id;
  private String name;
  private String email;
  private String source;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}