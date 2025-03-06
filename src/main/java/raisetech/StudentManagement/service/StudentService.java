package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public void addStudent(StudentDetail studentDetail){
    Student stundet = studentDetail.getStudent();
    stundet.setDeleted(false);
    repository.registerStudent(stundet);
  }

  public void addStudentCourse(StudentDetail studentDetail){
    List<StudentCourse> studentCourses = studentDetail.getStudentCourse();

    for(StudentCourse course : studentCourses){
    repository.registerStudentCourse(course);
    }
}
}