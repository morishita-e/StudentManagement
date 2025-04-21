package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exceptionHandler.DuplicateResourceException;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生一覧検索 受講生情報を取り扱うサービスです。 受講生の検索や登録、更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、受験指定は行いません
   *
   * @return 受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生一覧です IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します
   *
   * @return 受講生一覧(全件)
   */
  public StudentDetail searchStudentDetailById(int id) {
    Student student = repository.findStudentById(id);
    if (student == null) {
      throw new RuntimeException("受講生が見つかりません: " + id);
    }

    // コース情報を取得
    List<StudentCourse> studentCourse = repository.findCoursesByStudentId(id);

    return new StudentDetail(student, studentCourse);
  }

  /** 受講生詳細登録を行います。
   *受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値やコース開始日、コース終了日を設定します。
   *
   * @param  studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */

  @Transactional
  public StudentDetail addStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    student.setIsDeleted(false);

    // ✅ メールアドレスの重複チェック
    List<Student> existingStudents = repository.search();
    for (Student existing : existingStudents) {
      if (existing.getEMailAddress().equalsIgnoreCase(student.getEMailAddress())) {
        throw new DuplicateResourceException("このメールアドレスはすでに使用されています: " + student.getEMailAddress());
      }
    }

    // 受講生を登録
    repository.registerStudent(student); // 新規学生の登録

    // 登録後に学生IDを取得し、それをコース情報に設定する
    int studentId = student.getId();  // 登録された学生のID

    // 受講生コースリストに対して処理を行う
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      // コース名が空であればエラーをスロー
      if (studentCourse.getCourseName() == null || studentCourse.getCourseName().isEmpty()) {
        throw new IllegalArgumentException("コース名は必須です");
      }

      // studentIdが自動採番された値であることを確認し、設定
      studentCourse.setStudentId(studentId);  // 受講生IDを設定

      // コースの日付などの初期化
      initStudentsCourse(studentDetail, studentCourse);

      // 受講生コース情報を登録
      repository.registerStudentCourse(studentCourse);  // ここで自動採番されるはず
    });

    return studentDetail;
  }

  /** 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse 受講生コース情報
   * @param studentDetail 受講生情報詳細
   */

  private void initStudentsCourse(StudentDetail studentDetail, StudentCourse studentCourse) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(studentDetail.getStudent().getId());
    studentCourse.setStartday(now);
    studentCourse.setEndday(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。　受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */

  @Transactional
  public void updateStudentData(StudentDetail studentDetail) {

    Student student = studentDetail.getStudent();

    // 学生IDが存在しない場合、エラーをスロー
    if (repository.findStudentById(student.getId()) == null) {
      throw new RuntimeException(student.getId() + "は見つかりませんでした");
    }

    // 学生情報を更新
    repository.updateStudent(student);

    // コース情報がある場合は更新
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
  }

}