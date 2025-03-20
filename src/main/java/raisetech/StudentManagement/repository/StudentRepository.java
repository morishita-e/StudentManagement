package raisetech.StudentManagement.repository;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Mapper

public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> search();

  @Insert("INSERT INTO student (name, hurigana, nicName, eMailAddress, liveArea, age, gender, remark) VALUES (#{name}, #{hurigana}, #{nicName}, #{eMailAddress}, #{liveArea}, #{age}, #{gender}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT INTO student_course (studentId, courseName, startday, endday) VALUES (#{studentId}, #{courseName}, #{startday}, #{endday})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);


  @Select("SELECT * FROM student WHERE id = #{id}")
  Student findStudentById(@Param("id") int id);

  @Update("UPDATE student SET name = #{name}, hurigana = #{hurigana}, nicName = #{nicName}, eMailAddress = #{eMailAddress}, liveArea = #{liveArea}, age = #{age}, gender = #{gender}, remark = #{remark} WHERE id = #{id}")
    void updateStudent(Student student);

  @Select("SELECT * FROM student_course WHERE courseId = #{courseId}")
  StudentCourse findStudentCourseById(@Param("courseId") int courseId);

  @Select("SELECT * FROM student_course WHERE studentId = #{studentId}")
  List<StudentCourse> findCoursesByStudentId(@Param("studentId") int studentId);

  @Update("UPDATE student_course SET courseName = #{courseName}, startday = #{startday}, endday = #{endday} WHERE courseId = #{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

  @Select("SELECT courseId, studentId, courseName, startday, endday FROM student_course")
  List<StudentCourse> coursesearch();
}
