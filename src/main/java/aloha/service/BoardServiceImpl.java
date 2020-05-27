package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Board;
import aloha.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper mapper;

	@Override
	public List<Board> list() throws Exception {
		// TODO Auto-generated method stub
		return mapper.list();
	}

	@Override
	public void register(Board board) throws Exception {
		// TODO Auto-generated method stub
		mapper.create(board);
	}

	@Override
	public void modify(Board board) throws Exception {
		// TODO Auto-generated method stub
		mapper.update(board);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		mapper.delete(boardNo);
	}

	@Override
	public Board read(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return mapper.read(boardNo);
	}

	@Override
	public List<Board> search(String title) throws Exception {
		// TODO Auto-generated method stub
		return mapper.search(title);
	}

	@Override
	public String getPicture(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return mapper.getPicture(boardNo);
	}
	
}
