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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;
  private StudentRepository studentRepository;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter,StudentRepository studentRepository) {
    this.service = service;
    this.converter = converter;
    this.studentRepository = studentRepository;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    model.addAttribute("students", students);  // ここで "students" を渡していることを確認
    return "studentList";  // studentList.html に遷移
  }

  @GetMapping("/student/{id}")
  public String getStudentDetail(@PathVariable int id, Model model) {
    try {
      // 指定されたIDの受講生情報を取得
      Student student = service.searchStudentById(id);

      // 受講生のコース情報を取得
      List<StudentCourse> studentCourses = service.searchStudentcourseByStudentId(id);

      // `StudentDetail` にまとめる
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      studentDetail.setStudentCourse(studentCourses);

      // 画面にデータを渡す
      model.addAttribute("studentDetail", studentDetail);

      // studentDetail.html に遷移
      return "studentDetail";

    } catch (RuntimeException e) {
      // エラーメッセージを設定して、元のページに戻る
      model.addAttribute("errorMessage", "指定された学生が見つかりません。");
      return "redirect:/studentList";  // もしくは元のページにリダイレクト
    }
  }

  @GetMapping("/studentList_course")
  public String getStudentCourseList(Model model) {
    // 学生リストとコースリストを取得
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentcourse_List();
// 学生ごとにコースを関連付け
    model.addAttribute("studentCourseList",
        converter.convertStudentDetails(students, studentCourses));
    // studentCourseList.htmlにデータを渡す
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


  @GetMapping("/registerStudent")
  public String showRegisterStudentForm(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(new Student());
    studentDetail.setStudentCourse(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";  // フォームのHTMLテンプレート名
  }

  @GetMapping("/dataedit/{id}")
  public String editStudent(@PathVariable("id") String id, Model model) {
    Integer studentId = Integer.parseInt(id); // StringをIntegerに変換
    StudentDetail studentDetail = new StudentDetail();

    // 学生情報を取得
    Student student = studentRepository.findStudentById(studentId);
    studentDetail.setStudent(student);

    // 学生の受講コース情報を取得
    List<StudentCourse> studentCourses = studentRepository.findCoursesByStudentId(studentId);

    // 取得したstudentCoursesがnullでないことを確認
    if (studentCourses != null && !studentCourses.isEmpty()) {
      // startday と endday が null でないか確認
      for (StudentCourse studentCourse : studentCourses) {
        System.out.println("Startday: " + studentCourse.getStartday() + ", Endday: " + studentCourse.getEndday());
      }
    }

    studentDetail.setStudentCourse(studentCourses);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/dataedit/{id}")
  public String updateStudent(@PathVariable int id, @ModelAttribute StudentDetail studentDetail) {
    studentDetail.getStudent().setId(id);  // studentDetail の student に ID をセット
    service.updateStudentData(studentDetail);  // updateStudentData を呼び出し
    return "redirect:/studentList";        // 更新後にリダイレクト
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.addStudent(studentDetail);

    return "redirect:/studentList";
  }


}
