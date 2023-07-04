package com.mangos.restApi.repository;

import com.mangos.restApi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByLastName(String lastName);
    List<Student> findByLastNameAndFirstNameIsNotLikeAllIgnoreCase(String lastName, String firstName);

    @Query("Select s from Student s where s.firstName = 'Mango' ")
    List<Student> findStudentsWithNameMango();
}
