package com.example.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student_management.entity.Marks;

public interface MarksRepository extends JpaRepository<Marks, Long> {

}
