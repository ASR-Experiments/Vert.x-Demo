package com.asr.example.vert.x.demo.dto.http.response;


public class SayingDto {
  private long id;
  private String content;

  public SayingDto() {
    // Jackson deserialization
  }

  public SayingDto(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}
