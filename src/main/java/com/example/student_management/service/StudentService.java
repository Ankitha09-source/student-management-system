package com.example.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.student_management.entity.Student;
import com.example.student_management.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    public Student updateStudent(Long id, Student student) {

    Student existingStudent = studentRepository.findById(id).orElse(null);

    if (existingStudent == null) {
        return null;
    }

    existingStudent.setName(student.getName());
    existingStudent.setEmail(student.getEmail());
    existingStudent.setPhone(student.getPhone());
    existingStudent.setCourse(student.getCourse());
    existingStudent.setAddress(student.getAddress());

    return studentRepository.save(existingStudent);
}
    
}
