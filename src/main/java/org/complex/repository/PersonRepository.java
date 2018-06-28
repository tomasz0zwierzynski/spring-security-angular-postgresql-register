package org.complex.repository;

import java.util.ArrayList;
import java.util.List;

import org.complex.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long>{

	@Query(value = "SELECT * FROM public.people u WHERE u.firstname = ?1", nativeQuery = true)
	List<Person> findPerson(String firstName);
	
	@Query(value = "SELECT * FROM public.people u WHERE u.firstname = ?1 AND u.lastname = ?2", nativeQuery = true)
	List<Person> findPersonWith(String firstName, String lastName);
	
	@Query(value = "SELECT * FROM public.people u WHERE u.firstname LIKE %?1% OR u.lastname LIKE %?1%", nativeQuery = true)
	ArrayList<Person> searchPersons(String searchArgument);
}