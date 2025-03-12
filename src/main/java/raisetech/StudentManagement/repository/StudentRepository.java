package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
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


  @Select("SELECT courseId, studentId, courseName, startday, endday FROM student_course")
  List<StudentCourse> coursesearch();
}
