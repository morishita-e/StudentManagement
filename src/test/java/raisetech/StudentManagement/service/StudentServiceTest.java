package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    when(repository.search()).thenReturn(Collections.emptyList());// メールアドレスの重複はなし

    doAnswer(invocation -> {
      Student savedStudent = invocation.getArgument(0);
      savedStudent.setId(1); // 仮IDを設定
      return null;
    }
    ).when(repository).registerStudent(any(Student.class));       // 学生登録時にIDをセット

    doNothing().when(repository).registerStudentCourse(any(StudentCourse.class)); // コース登録モック

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
  void 受講生詳細更新_複数コースの場合も正常に更新される() {
    // モックデータ準備
    Student student = new Student();
    student.setId(1);
    student.setName("Updated Name");

    // コースを2件用意
    StudentCourse course1 = new StudentCourse();
    course1.setStudentId(1);
    course1.setCourseName("Updated Course 1");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentId(1);
    course2.setCourseName("Updated Course 2");

    List<StudentCourse> studentCourses = Arrays.asList(course1, course2);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    // モック設定
    when(repository.findStudentById(1)).thenReturn(student);
    doNothing().when(repository).updateStudent(student);
    doNothing().when(repository).updateStudentCourse(course1);
    doNothing().when(repository).updateStudentCourse(course2);

    // テスト対象実行
    sut.updateStudentData(studentDetail);

    // 検証
    verify(repository).findStudentById(1);
    verify(repository).updateStudent(student);
    verify(repository).updateStudentCourse(course1);
    verify(repository).updateStudentCourse(course2);
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

  @Test
  void 受講生詳細更新_正常に更新がされる場合() {
    // モックデータ準備
    Student student = new Student();
    student.setId(1);                    // IDをセット
    student.setName("Updated Name");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("Updated Course");

    List<StudentCourse> studentCourses = Arrays.asList(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourses);

    // モックリポジトリの設定
    when(repository.findStudentById(1)).thenReturn(student);         // 学生が存在するとする
    doNothing().when(repository).updateStudent(student);             // 学生更新処理モック
    doNothing().when(repository).updateStudentCourse(studentCourse); // コース更新処理モック

    // テスト対象メソッドを呼び出し
    sut.updateStudentData(studentDetail);

    // 呼び出されたか検証
    verify(repository).findStudentById(1);
    verify(repository).updateStudent(student);
    verify(repository).updateStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細更新_存在しないIDによるエラー発生() {
    // モックデータ準備
    Student student = new Student();
    student.setId(999);                // 存在しないID
    student.setName("Name");

    StudentDetail studentDetail = new StudentDetail(student, Collections.emptyList());

    // モックリポジトリの設定
    when(repository.findStudentById(999)).thenReturn(null); // 該当なし

    // 例外が発生するか確認
    RuntimeException exception = assertThrows(RuntimeException.class, () -> sut.updateStudentData(studentDetail));

    assertTrue(exception.getMessage().contains("999は見つかりませんでした"));
  }
}