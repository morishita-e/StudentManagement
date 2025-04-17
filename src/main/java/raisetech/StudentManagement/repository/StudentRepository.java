package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルを紐づくRepositoryです。
 */

@Mapper

public interface StudentRepository {

  /**
   * 受講生の全件検索
   *
   * @return 受講生一覧(全件)
   */
  List<Student> search();

  /**
   * 受講生検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */

  Student findStudentById(@Param("id") int id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */

  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */

  List<StudentCourse> searchStudentCourse(@Param("studentId") int studentId);

  /**
   * 受講生を新規登録します。　IDに関して自動採番を行う。
   *
   * @return student 受講生
   */

  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。　IDに関して自動採番を行う。
   *
   * @return studentCourse 受講生コース情報
   */

  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param  student 受講生
   */

  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param  studentCourse 受講生コース情報
   */

  void updateStudentCourse(StudentCourse studentCourse);

  // メールアドレスから受講生を検索
  Student findStudentByEmail(@Param("email") String email);

  List<StudentCourse> findCoursesByStudentId(@Param("studentId") int studentId);
}
