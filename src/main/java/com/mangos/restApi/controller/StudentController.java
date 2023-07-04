package com.mangos.restApi.controller;

import com.mangos.restApi.model.Student;
import com.mangos.restApi.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @GetMapping
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody @Valid Student student) {
        return studentRepository.save(student);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id){
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    //    @GetMapping("/{id}")
//    public ResponseEntity<Student> getStudent(@PathVariable Long id){
//        Optional<Student> studentOptional = studentRepository.findById(id);
//
//        if (studentOptional.isPresent()){
//            return ResponseEntity.ok(studentOptional.get());
//        }else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                   return ResponseEntity.ok().build();
                }).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> putStudent(@PathVariable Long id, @RequestBody @Valid Student student){
        return studentRepository.findById(id)
                .map(studentFromDB -> {
                    studentFromDB.setFirstName(student.getFirstName());
                    studentFromDB.setLastName(student.getLastName());
                    studentFromDB.setEmail(student.getEmail());
                    return ResponseEntity.ok().body(studentRepository.save(studentFromDB));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(@PathVariable Long id, @RequestBody Student student){
        return studentRepository.findById(id)
                .map(studentFromDB -> {
                    if(!student.getFirstName().isEmpty()){
                        studentFromDB.setFirstName(student.getFirstName());
                    }
                    if(!student.getLastName().isEmpty()){
                        studentFromDB.setLastName(student.getLastName());
                    }
                    if(!student.getEmail().isEmpty()){
                        studentFromDB.setEmail(student.getEmail());
                    }

                    return ResponseEntity.ok().body(studentRepository.save(studentFromDB));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //metoda nie jest wydajna wyciąga z bazy wszystkich i filtruje w java
//    @GetMapping("/lastname")
//    public List<Student> findStudent(@RequestParam String lastname){
//        return studentRepository.findAll().stream()
//                .filter(student -> student.getLastName().equals(lastname))
//                .collect(Collectors.toList());
//    }

    // ta metoda jest lepsza po szuka studentów bezposrednio w bazie
    @GetMapping("/lastname")
    public List<Student> findStudent(@RequestParam String lastname){
        return studentRepository.findByLastName(lastname);
    }

    @GetMapping("/find")
    public List<Student> findStudent2(@RequestParam String lastname, @RequestParam String firstname){
        return studentRepository.findByLastNameAndFirstNameIsNotLikeAllIgnoreCase(lastname, firstname);
    }

    @GetMapping("/mangi")
    public List<Student> findStudent3(){
        return studentRepository.findStudentsWithNameMango();
    }
}
