package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.StudentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
受講生の検索や登録、更新などを行うREST APIを受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;
  private StudentRepository studentRepository;

  @Autowired
  public StudentController(StudentService service, StudentRepository studentRepository) {
    this.service = service;
    this.studentRepository = studentRepository;
  }

  /** 受講生詳細の一覧検索です。全件検索を行うので、受験指定は行いません
   *
   * @return 受講生一覧(全件)
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /** 受講生詳細の索です。IDに紐づく任意の受講生情報を取得します。
   *
   * @param  id 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudentDetail(@PathVariable @Min(1) @Max(99999) int id) {
    return service.searchStudentDetailById(id);
  }

  /** 受講生詳細の登録を行います。
   *
   * @param  studentDetail 受講生詳細
   * @return 実行結果
   */
@Operation(summary = "受講生登録", description = "受講生を登録します")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail>  registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.addStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。　キャンセルフラグの更新もここで行います(論理削除)　
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */

  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail){
    service.updateStudentData(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

}
