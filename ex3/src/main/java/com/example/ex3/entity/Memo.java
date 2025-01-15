package com.example.ex3.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_memo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;

  @Column(length = 200, nullable = false)
  private String memoText;

  @Override
  public String toString() {
    return mno+":"+memoText;
  }

  public Long getMno() {
    return mno;
  }

  public void setMno(Long mno) {
    this.mno = mno;
  }

  public String getMemoText() {
    return memoText;
  }

  public void setMemoText(String memoText) {
    this.memoText = memoText;
  }
}
