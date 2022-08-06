package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.controllers.exceptions.DatabaseExceptions;
import com.devsuperior.bds02.controllers.exceptions.DependentComponentException;
import com.devsuperior.bds02.controllers.exceptions.ResourceNotFoundException;
import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Transactional(readOnly=true)
	public List<EventDTO> findAll(Pageable pageable){
		Page<Event> list = eventRepository.findAll(pageable);
		return list.stream().map(x -> new EventDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity = eventRepository.save(entity);
		return new EventDTO(entity);
	}
	
	
	public void delete(Long id){
		try{eventRepository.deleteById(id);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not Found " + id);
		}catch(EmptyResultDataAccessException e) {
			throw new DatabaseExceptions("Id not found");
		}catch(DataIntegrityViolationException e){
			throw new DependentComponentException("Forbiden Id has dependencies!");
		}
	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = eventRepository.getReferenceById(id);
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			City city = cityRepository.getReferenceById(dto.getCityId());
			entity.setCity(city);
			entity = eventRepository.save(entity);
			return new EventDTO(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found");
		}	
	}	
}
