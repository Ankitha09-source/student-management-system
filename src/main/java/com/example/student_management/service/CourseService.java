package com.example.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.student_management.entity.Course;
import com.example.student_management.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // ADD COURSE
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // GET ALL COURSES
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // GET COURSE BY ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    // DELETE COURSE
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // UPDATE COURSE
    public Course updateCourse(Long id, Course course) {

        Course existingCourse =
                courseRepository.findById(id).orElse(null);

        if (existingCourse == null) {
            return null;
        }

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setDuration(course.getDuration());

        return courseRepository.save(existingCourse);
    }
}