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
import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;
	
	@Transactional(readOnly=true)
	public List<CityDTO> findAll(Pageable pageable){
		Page<City> list = cityRepository.findAll(pageable);
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity = cityRepository.save(entity);
		return new CityDTO(entity);
	}
	
	
	public void delete(Long id){
		try{cityRepository.deleteById(id);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not Found " +id);
		}catch(EmptyResultDataAccessException e) {
			throw new DatabaseExceptions("Id not found");
		}catch(DataIntegrityViolationException e){
			throw new DependentComponentException("Forbiden Id has dependencies!");
		}
	}
	
	
	
}
