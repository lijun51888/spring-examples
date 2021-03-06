package examples.controller;

import examples.model.Employee;
import examples.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class RestControllerEmployee {
    @Autowired
    EmployeeService service;

    @RequestMapping(value="/employees/{id}", method=RequestMethod.GET)
    public Employee employee(@PathVariable int id) {
        return service.getById(id);
    }

    @RequestMapping(value="/employees", method=RequestMethod.GET)
    public List<Employee> employeeList() {
        return service.getAll();
    }

    @RequestMapping(value="/secret", method=RequestMethod.GET)
    public String secret() {
        return "secret";
    }

    @RequestMapping(value="/employees", method=RequestMethod.POST)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee, UriComponentsBuilder ucBuilder) {
        int insertedId = service.create(employee);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/employees/{id}").buildAndExpand(insertedId).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
