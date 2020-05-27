package aloha.service;

import java.util.List;

import aloha.domain.Board;

public interface BoardService {
	public List<Board> list() throws Exception;
	public void register(Board board) throws Exception;
	public void modify(Board board) throws Exception;
	public void remove(Integer boardNo) throws Exception;
	public Board read(Integer boardNo) throws Exception;
	public List<Board> search(String title) throws Exception;
	public String getPicture(Integer boardNo) throws Exception;
}
