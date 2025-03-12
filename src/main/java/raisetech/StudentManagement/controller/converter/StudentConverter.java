package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourse> studentCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourses = studentCourses.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentCourse(convertStudentCourses);
      studentDetails.add(studentDetail);
    });

    return studentDetails;
  }

  public static StudentDetail createStudentDetail(Student student) {
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);

    return detail;
  }

  //public static StudentDetail createStudentCourseDetail(StudentCourse studentCourse) {
    //StudentDetail detail = new StudentDetail();
    //List<StudentCourse> courses = new ArrayList<>();
    //courses.add(studentCourse);
    //detail.setStudentCourse(courses);
    //return detail;
  //}
}



