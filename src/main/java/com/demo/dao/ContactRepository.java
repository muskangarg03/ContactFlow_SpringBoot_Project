package com.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.entities.Contact;
import com.demo.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	//Pagination...
	//A page is a sublist of a list of objects.
	//Pageable contains : current page, contacts per page
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pePageable);
	//public List<Contact> findContactsByUser(@Param("userId")int userId);
	
	
	//search method
	public List<Contact> findByNameContainingAndUser(String keywords, User user);   //return all contacts that contains given keywords
	
	
}
