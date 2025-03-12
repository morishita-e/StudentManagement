package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentCourse> searchStudentcourse_List() {
    return repository.coursesearch();
  }

  @Transactional
  public void addStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    student.setDeleted(false);
    repository.registerStudent(student);
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      studentCourse.setStartday(LocalDateTime.now());
      studentCourse.setEndday(LocalDateTime.now().plusYears(1));

      repository.registerStudentCourse(studentCourse);
    }
  }

  //public void addStudentCourse(StudentDetail studentDetail) {
  //List<StudentCourse> studentCourses = studentDetail.getStudentCourse();

  //for (StudentCourse course : studentCourses) {
  //repository.registerStudentCourse(course);
  //}
  //}
}