package com.example.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.student_management.entity.Marks;
import com.example.student_management.repository.MarksRepository;

@Service
public class MarksService {

    private final MarksRepository marksRepository;

    public MarksService(MarksRepository marksRepository) {
        this.marksRepository = marksRepository;
    }

    public Marks addMarks(Marks marks) {
        return marksRepository.save(marks);
    }

    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }

    public Marks getMarksById(Long id) {
        return marksRepository.findById(id).orElse(null);
    }

    public void deleteMarks(Long id) {
        marksRepository.deleteById(id);
    }
    public Marks updateMarks(Long id, Marks marks) {

    Marks existingMarks = marksRepository.findById(id).orElse(null);

    if (existingMarks == null) {
        return null;
    }

    existingMarks.setStudentId(marks.getStudentId());
    existingMarks.setSubject(marks.getSubject());
    existingMarks.setMarks(marks.getMarks());

    return marksRepository.save(existingMarks);
}
}
