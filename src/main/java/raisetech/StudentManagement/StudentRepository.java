package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper

public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> search();

  @Select("SELECT course_id, student_id, course_name, startday, endday FROM student_course")
  @Results({
      @Result(property = "courseId", column = "course_id"),
      @Result(property = "studentId", column = "student_id"),
      @Result(property = "courseName", column = "course_name"),
      @Result(property = "startday", column = "startday", javaType = java.time.LocalDateTime.class),
      @Result(property = "endday", column = "endday", javaType = java.time.LocalDateTime.class)
  })
  List<Student_course> course_search();
}
