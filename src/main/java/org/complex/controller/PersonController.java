package org.complex.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.complex.model.Person;
import org.complex.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class PersonController {

	public static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	PersonRepository repository;
	
	@RequestMapping("/generate")
	public ResponseEntity<String> process(){
		logger.info("Generating 5 users...");
		// save a list of Customers // SaveAll???
		repository.saveAll(Arrays.asList(new Person("Piotrek", "Kwadrat"), new Person("Franek", "Kwadrat"),
										new Person("Dawid", "Tensor"), new Person("Tomasz", "Zappa"),
										new Person("Anna", "Kopycka"), new Person("Random", "Guy")));
		return new ResponseEntity<String>("Generated example users.",HttpStatus.CREATED);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> getAllPersons(){
		logger.info("Retrieving all users.");
		ArrayList<Person> result = new ArrayList<>();
		for(Person user : repository.findAll()){
			result.add(user);
		}
		return new ResponseEntity<ArrayList<Person>>(result,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPersonById(@PathVariable("id") long id){
		logger.info("Retrieving user id={}",id);
		Optional<Person> user = repository.findById(id);
		if(user.isPresent()) {
			return new ResponseEntity<Person>(user.get(),HttpStatus.OK);
		}
		return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/search/", method = RequestMethod.GET)
	public ResponseEntity<?> getSearchedPerson(@RequestParam(value="term", required=true) String term){
		logger.info("Searching for user term={}",term);
		ArrayList<Person> usersFound = repository.searchPersons(term);
		logger.info("users Found: {}",usersFound.size());
		if (!(usersFound.isEmpty())) {
			return new ResponseEntity<ArrayList<Person>>(usersFound,HttpStatus.OK);
		}
		return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> createPerson(@RequestBody Person user, UriComponentsBuilder ucBuilder){
		logger.info("Creating new user...");
        if ( !(repository.findPersonWith(user.getFirstName(), user.getLastName())).isEmpty()) { 
            logger.error("Unable to create. A Person with name {} {} already exist", user.getFirstName(),user.getLastName());
            return new ResponseEntity<String>("Person already in the database!",HttpStatus.CONFLICT);
        }
		Person newPerson = new Person(user.getFirstName(), user.getLastName());
		repository.save(newPerson);
										//tutaj już newPerson ma id z bazy wygenerowane
    	return new ResponseEntity<Person>(newPerson, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Person> updatePersonById(@PathVariable("id") long id, @RequestBody Person updatedPerson){
		logger.info("Updating user id={}",id);
		Optional<Person> user = repository.findById(id);
		user.get().setFirstName(updatedPerson.getFirstName());
		user.get().setLastName(updatedPerson.getLastName());
		repository.save(user.get());
		return new ResponseEntity<Person>(user.get(),HttpStatus.OK);
	}
	
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deletePerson(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Person with id " + id);
 
        Optional<Person> user = repository.findById(id);
        if (!user.isPresent()) {
            System.out.println("Unable to delete. Person with id " + id + " not found");
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        }
 
        repository.deleteById(id);
        return new ResponseEntity<Person>(HttpStatus.NO_CONTENT);
    }
}