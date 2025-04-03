package raisetech.StudentManagement.controller;

import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.StudentService;

@RestController
@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;
  private StudentRepository studentRepository;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      StudentRepository studentRepository) {
    this.service = service;
    this.converter = converter;
    this.studentRepository = studentRepository;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();

    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      List<StudentCourse> studentCourses = service.searchStudentCourse(student.getId());
      studentDetails.add(converter.convertStudentDetail(student, studentCourses));
    }

    return studentDetails;
  }

  @GetMapping("/student/{id}")
  public StudentDetail getStudentDetail(@PathVariable int id) {
    return service.searchStudentDetailById(id);
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
  public String editStudent(@PathVariable("id") int id, Model model) {
    StudentDetail studentDetail = new StudentDetail();

    // 学生情報を取得
    Student student = studentRepository.findStudentById(id);
    studentDetail.setStudent(student);

    // 学生の受講コース情報を取得
    List<StudentCourse> studentCourses = studentRepository.findCoursesByStudentId(id);
    studentDetail.setStudentCourse(studentCourses);

    // isDeleted の状態をモデルに渡す
    model.addAttribute("studentDetail", studentDetail);
    model.addAttribute("isDeleted", student.getIsDeleted());

    return "updateStudent";
  }

  @PostMapping("/dataedit/{id}")
  public String updateStudent(@PathVariable int id, @ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    // バリデーションエラーがあればフォームに戻す
    if (result.hasErrors()) {
      return "updateStudent";  // エラーメッセージを表示するため、フォームに戻す
    }

    studentDetail.getStudent().setId(id);  // studentDetail の student に ID をセット

    try {
      service.updateStudentData(studentDetail);  // 学生情報の更新
      return "redirect:/studentList";        // 更新後にリダイレクト
    } catch (Exception e) {
      e.printStackTrace();
      return "errorPage";   // エラーが発生した場合、エラーページに遷移
    }
  }

  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail>  registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.addStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail){
    service.updateStudentData(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

/*
  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    model.addAttribute("students", students);  // ここで "students" を渡していることを確認
    return "studentList";  // studentList.html に遷移
  }

  @PostMapping("/updateStudent")
  public String updateStudent(
      @ModelAttribute StudentDetail studentDetail,
      @RequestParam(value = "cancelUpdate", required = false) String cancelUpdate
  ) {
    try {
      Student student = studentDetail.getStudent();
      List<StudentCourse> studentCourses = studentDetail.getStudentCourse();

      // `cancelUpdate` が null なら false（チェックなし）
      student.setIsDeleted(cancelUpdate != null);

      // 学生情報を更新
      studentRepository.updateStudent(student);

      // 受講コースの更新処理
      for (StudentCourse course : studentCourses) {
        if (course.getCourseId() != null) {  // 既存のコース情報がある場合は更新
          studentRepository.updateStudentCourse(course);
        } else {  // 新しいコースを追加
          studentRepository.addStudentCourse(course);
        }
      }

      return "redirect:/studentList";  // リストにリダイレクト
    } catch (Exception e) {
      e.printStackTrace();
      return "errorPage";  // エラーページにリダイレクト
    }
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
*/
}
