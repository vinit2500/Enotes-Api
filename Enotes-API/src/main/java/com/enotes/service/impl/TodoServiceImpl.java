package com.enotes.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.entity.Todo;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.TodoRepository;
import com.enotes.service.TodoService;
import com.enotes.util.CommonUtils;
import com.enotes.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validation validation;

	@Override
	public Boolean saveTodo(TodoDto todoDto) throws Exception {

		// validate Todo status
		validation.todoValidation(todoDto);
		Todo todo = modelMapper.map(todoDto, Todo.class);

		todo.setStatusId(todoDto.getStatus().getId());
		Todo savedTodo = todoRepository.save(todo);

		if (!ObjectUtils.isEmpty(savedTodo)) {
			return true;
		}
		return false;
	}

	@Override
	public TodoDto getTodoById(Integer id) throws Exception {
		Todo todo = todoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found woth this id"));
		TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
		setStatus(todoDto, todo);
		return todoDto;
	}

	@Override
	public List<TodoDto> getTodoByUser() {

		Integer userId = CommonUtils.getLoggedInUser().getId();
		List<Todo> todos = todoRepository.findByCreatedBy(userId);
		List<TodoDto> listOfTodos = todos.stream().map(td -> modelMapper.map(td, TodoDto.class)).toList();
		return listOfTodos;
	}

	private void setStatus(TodoDto todoDto, Todo todo) {
		
		for(TodoStatus st : TodoStatus.values()) {
		     if(st.getId().equals(todo)) {
		    	 StatusDto statusDto = new StatusDto();
		    	 statusDto.setId(st.getId());
		    	 statusDto.setName(st.getName());
		    	 todoDto.setStatus(statusDto);
		     }
		}

	}
}
