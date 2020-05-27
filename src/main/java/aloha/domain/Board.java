package aloha.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Board {
	private int boardNo;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private String filePath;
	private MultipartFile file;
}
