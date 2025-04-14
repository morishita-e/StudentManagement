package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルを紐づくRepositoryです。
 */

@Mapper

public interface StudentRepository {

  /**
   * 受講生一覧検索
   *
   * @return 受講生一覧(全件)
   */
  @Select("SELECT * FROM student WHERE isDeleted = false")
  List<Student> search();

  /**
   * 受講生検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生
   */

  @Select("SELECT * FROM student WHERE id = #{id}")
  Student findStudentById(@Param("id") int id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報(全件)
   */

  @Select("SELECT * FROM student_course")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */

  @Select("SELECT * FROM student_course WHERE studentId = #{studentId}")
  List<StudentCourse> searchStudentCourse(@Param("studentId") int studentId);

  /**
   * 受講生を新規登録します。　IDに関して自動採番を行う。
   *
   * @return student 受講生
   */

  @Insert("INSERT INTO student (name, hurigana, nicName, eMailAddress, liveArea, age, gender, remark) VALUES (#{name}, #{hurigana}, #{nicName}, #{eMailAddress}, #{liveArea}, #{age}, #{gender}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。　IDに関して自動採番を行う。
   *
   * @return studentCourse 受講生コース情報
   */

  @Insert("INSERT INTO student_course (studentId, courseName, startday, endday) VALUES (#{studentId}, #{courseName}, #{startday}, #{endday})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param  student 受講生
   */

  @Update("UPDATE student SET name = #{name}, hurigana = #{hurigana}, nicName = #{nicName}, eMailAddress = #{eMailAddress}, liveArea = #{liveArea}, age = #{age}, gender = #{gender}, remark = #{remark}, isDeleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param  studentCourse 受講生コース情報
   */

  @Update("UPDATE student_course SET courseName = #{courseName} WHERE courseId = #{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

  @Select("SELECT courseId, studentId, courseName, startday, endday FROM student_course WHERE studentId = #{studentId}")
  List<StudentCourse> findCoursesByStudentId(@Param("studentId") int studentId);
}
