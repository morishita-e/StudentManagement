package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exceptionHandler.DuplicateResourceException;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private  StudentService sut;

  @BeforeEach
  void  before() {
    sut = new StudentService(repository, converter);
  }
  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が呼び出させていること(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

when(repository.search()).thenReturn(studentList);
when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

  }

  @Test
  void 受講生の一覧検索__受講生が存在する場合_受講生詳細を返す(){
int studentId = 1;
Student student = new Student();
List<StudentCourse> studentCourses = new ArrayList<>();

    when(repository.findStudentById(studentId)).thenReturn(student);
    when(repository.findCoursesByStudentId(studentId)).thenReturn(studentCourses);

StudentDetail result = sut.searchStudentDetailById(studentId);

assertNotNull(result);
assertEquals(student, result.getStudent());
assertEquals(studentCourses, result.getStudentCourseList());

verify(repository, times(1)).findStudentById(studentId);
verify(repository, times(1)).findCoursesByStudentId(studentId);


  }

  @Test
  void 受講生IDで検索_受講生が存在しない場合_RuntimeExceptionをスローする() {
    int studentId = 1;

    when(repository.findStudentById(studentId)).thenReturn(null);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      sut.searchStudentDetailById(studentId);
    });

    assertEquals("受講生が見つかりません: " + studentId, exception.getMessage());
    verify(repository, times(1)).findStudentById(studentId);
    verify(repository, times(0)).findCoursesByStudentId(studentId); // 呼ばれないはず！
  }

  @Test
  void 受講生詳細登録_正常に登録がされる場合(){
    Student student = new Student();
    student.setEMailAddress("Test@example.com");
    student.setName("Test test");
    student.setIsDeleted(false);

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストの実験コース");

    List<StudentCourse> studentCourses = Arrays.asList(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    // モックリポジトリの設定
    when(repository.search()).thenReturn(Collections.emptyList());      // メールアドレスの重複はなし
    doNothing().when(repository).registerStudent(student);              // 学生登録処理のモック
    doNothing().when(repository).registerStudentCourse(studentCourse);  // コース登録処理のモック

    // サービスメソッドの呼び出し
    StudentDetail result = sut.addStudent(studentDetail);


    verify(repository).search();                              // メールアドレスの重複チェックが行われたか
    verify(repository).registerStudent(student);              // 学生が登録されたか
    verify(repository).registerStudentCourse(studentCourse);  // コースが登録されたか

// 返り値がnullでないことを確認
    assertNotNull(result);

    // 学生の名前が正しいことを確認
    assertEquals("Test test", result.getStudent().getName());
  }

  @Test
  void 受講生詳細登録_メールアドレス重複エラー() {
    // モックデータの準備
    Student student = new Student();
    student.setEMailAddress("Test@example.com");
    student.setName("Test test");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストの実験コース");

    List<StudentCourse> studentCourses = Arrays.asList(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    // モックリポジトリの設定
    Student existingStudent = new Student();
    existingStudent.setEMailAddress("Test@example.com");
    when(repository.search()).thenReturn(Collections.singletonList(existingStudent));    // 既存のメールアドレスが返される

    // サービスメソッドの呼び出し
    assertThrows(DuplicateResourceException.class, () -> sut.addStudent(studentDetail)); // 例外がスローされることを確認
  }

  @Test
  void 受講生詳細登録_コース名が空に場合() {
    // モックデータの準備
    Student student = new Student();
    student.setEMailAddress("Test@example.com");
    student.setName("Test test");

    StudentCourse studentCourse = new StudentCourse();

    // コース名が空
    studentCourse.setCourseName("");

    List<StudentCourse> studentCourses = Arrays.asList(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    // モックリポジトリの設定
    when(repository.search()).thenReturn(Collections.emptyList());                     //メールアドレスの重複はなし

    // サービスメソッドの呼び出し
    assertThrows(IllegalArgumentException.class, () -> sut.addStudent(studentDetail)); // 例外がスローされることを確認
  }

  @Test
  void 受講生コース情報登録_初期情報(){
    Student student = new Student();

    // 受講生IDを設定
    student.setId(1);

    student.setName("Test test");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストの実験コース");

    List<StudentCourse> studentCourses = Arrays.asList(studentCourse);        // 学生のコースリストを作成
    StudentDetail studentDetail = new StudentDetail(student, studentCourses); // StudentDetail インスタンスを作成

    LocalDateTime now = LocalDateTime.now();                      // 現在の日付を取得

// サービスメソッドの呼び出し
    StudentDetail result = sut.addStudent(studentDetail);        // addStudentメソッドを呼び出し
    assertEquals(student.getId(), studentCourse.getStudentId()); // 受講生IDが設定されていること

    // 開始日が現在の日付と一致または後であること
    assertTrue(studentCourse.getStartday().isEqual(now) || studentCourse.getStartday().isAfter(now));

    // 終了日が1年後であること
    assertTrue(studentCourse.getEndday().isEqual(now.plusYears(1)) || studentCourse.getEndday().isAfter(now.plusYears(1)));

  }
}