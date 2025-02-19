package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentList") public List<Student> getStudentList() {
    return service.searchStudentList();
  }


  @GetMapping("/studentList_course")
  public List<StudentCourse> getStudentcourse_List() {
    return service.searchStudentcourse_List();
  }

  @GetMapping("/studentsearch3039")
  public List<Student> getStudentList3039() {
    return service.getStudentList3039();
  }

  @GetMapping("/studentcoursejava")
  public List<StudentCourse> getStudentListJava() {
    return service.getStudentListJava();
  }
}
