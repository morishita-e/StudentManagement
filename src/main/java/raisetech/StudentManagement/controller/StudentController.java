package raisetech.StudentManagement.controller;

import org.springframework.ui.Model;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentcourse_List();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
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
