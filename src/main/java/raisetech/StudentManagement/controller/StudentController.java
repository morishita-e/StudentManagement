package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
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
  public String getStudentCourseList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentcourse_List();

    model.addAttribute("studentCourseList",
        converter.convertStudentDetails(students, studentCourses));
    return "studentCourseList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(new Student());
    studentDetail.setStudentCourse(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  //@GetMapping("/newStudentCourse")
  //public String newStudentCourse(Model model) {
    //StudentDetail studentDetail = new StudentDetail();
    //studentDetail.setStudentCourse(new ArrayList<>());
    //studentDetail.getStudentCourse().add(new StudentCourse());
    //model.addAttribute("studentDetail", studentDetail);
    //return "registerStudentCourse";
  //}

  @GetMapping("/registerStudent")
  public String showRegisterStudentForm(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";  // フォームのHTMLテンプレート名
  }

 // @GetMapping("/registerStudentCourse")
  //public String showRegisterStudentCourseForm(Model model) {
    //StudentDetail studentDetail = new StudentDetail();
    //studentDetail.setStudentCourse(new ArrayList<>());
    //studentDetail.getStudentCourse().add(new StudentCourse()); // 初期値として1つのコースを追加
    //model.addAttribute("studentDetail", studentDetail);
    //return "registerStudentCourse";  // フォーム表示用のテンプレート
  //}

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.addStudent(studentDetail);

    return "redirect:/studentList";
  }

 // @PostMapping("/registerStudentCourse")
  //public String registerStudentCourse(@ModelAttribute StudentDetail studentDetail,
    //  BindingResult result) {
    //if (result.hasErrors()) {
     // return "registerStudentCourse";
   // }

   // service.addStudentCourse(studentDetail);

    //return "redirect:/studentList";
  //}
}
