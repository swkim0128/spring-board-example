package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Board;

@Mapper
public interface BoardMapper {
	public List<Board> list() throws Exception;
	public void create(Board board) throws Exception;
	public Board read(Integer boardNo) throws Exception;
	public void update(Board board) throws Exception;
	public void delete(Integer boardNo) throws Exception;
	public List<Board> search(String title) throws Exception;
	public String getPicture(Integer boardNo) throws Exception;
}
