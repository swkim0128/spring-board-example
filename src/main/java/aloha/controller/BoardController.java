package aloha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import aloha.domain.Board;
import aloha.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private BoardService service;
	
	// localhost:8080/board/list
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public void list(Model model) throws Exception {
		model.addAttribute("board", new Board());
		model.addAttribute("list", service.list());
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(Board board, Model model) throws Exception {
		MultipartFile file = board.getFile();
		
		// file 정보 확인
		log.info("originalName : " + file.getOriginalFilename());
		log.info("size : " + file.getSize());
		log.info("contentType : " + file.getContentType());
		
		String createdFileName = uploadFile(file.getOriginalFilename(), file.getBytes());
		
		board.setFilePath(createdFileName);
		
		log.info("filePath : " + createdFileName);
		
		service.register(board);
		model.addAttribute("msg", "등록이 완료되었습니다.");
		
		return "board/success";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public void registerForm(Board board, Model model) throws Exception {
		
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(Board board, Model model) throws Exception {
		service.modify(board);
		model.addAttribute("msg", "수정이 완료되었습니다.");
		
		return "board/success";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public void modifyForm(int boardNo, Model model) throws Exception {
		model.addAttribute(service.read(boardNo));
	}

	@RequestMapping(value="/read", method=RequestMethod.GET)
	public void read(@RequestParam("boardNo") int boardNo, Model model) throws Exception {
		model.addAttribute(service.read(boardNo));
		
		// media(사진), doc(문서)
		String fileName = service.getPicture(boardNo);
		MediaType fileType = null;
		
		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileType = getMediaType(formatName);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("fileType", fileType);
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public String remove(@RequestParam("boardNo") int boardNo, Model model) throws Exception {
		service.remove(boardNo);
		model.addAttribute("msg", "삭제가 완료되었습니다.");
		
		return "board/success";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(String title, Model model) throws Exception {
		Board board = new Board();
		board.setTitle(title);
		model.addAttribute("board", board);
		model.addAttribute("list", service.search(title));
		
		return "board/list";
	}
	
	private String uploadFile(String originalName, byte[] fileDatas) throws Exception {
		UUID uid = UUID.randomUUID();
		
		String createdFileName = uid.toString() + "_" + originalName;
		
		File target = new File(uploadPath, createdFileName);
		
		FileCopyUtils.copy(fileDatas, target);
		
		return createdFileName;
	}
	
	@ResponseBody
	@RequestMapping("/display")
	public ResponseEntity<byte[]> display(Integer boardNo) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		String fileName = service.getPicture(boardNo);
		
		log.info("FILE NAME : " + fileName);
		
		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			MediaType mType = getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			
			in = new FileInputStream(uploadPath + File.separator + fileName);
			
			if(mType != null) {
				headers.setContentType(mType);
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		}
		catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	private MediaType getMediaType(String formatName) {
		if(formatName != null) {
			if(formatName.contentEquals("jpg")) {
				return MediaType.IMAGE_JPEG;
			}
			if(formatName.contentEquals("png")) {
				return MediaType.IMAGE_PNG;
			}
			if(formatName.contentEquals("gif")) {
				return MediaType.IMAGE_GIF;
			}
		}
		
		return null;
	}
}
