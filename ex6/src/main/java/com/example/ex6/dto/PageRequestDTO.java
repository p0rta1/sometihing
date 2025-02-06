package com.example.ex6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
  private int page; // 요청한 페이지 번호
  private int size; // 한페이지당 갯수
  private String type;
  private String keyword;

  public PageRequestDTO() {
    this.page = 1;this.size = 10;
  }

  // 이건 내장된 인터페이스 pageble 입니까? -> Y
  public Pageable getPageable(Sort sort) {
    return PageRequest.of(page - 1, size, sort);
  }
}
