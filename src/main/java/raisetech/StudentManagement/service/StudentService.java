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

  @Transactional
  public void updateStudentData(StudentDetail studentDetail) {

    Student student = studentDetail.getStudent();

    // 学生IDが存在しない場合、エラーをスロー
    if (repository.findStudentById(student.getId()) == null) {
      throw new RuntimeException(student.getId() + "は見つかりませんでした");
    }

    // 学生情報を更新
    repository.updateStudent(student);

    // コース情報は編集されない場合、処理を行わない
    if (studentDetail.getStudentCourse() != null && !studentDetail.getStudentCourse().isEmpty()) {
      // ここでコース情報が存在しない場合はスキップ
      return;
    }
  }

  // 学生IDに基づいてコースを取得するメソッド
  public List<StudentCourse> searchStudentcourseByStudentId(int studentId) {
    return repository.findCoursesByStudentId(studentId);  // repository からコースリストを取得
  }


  public Student searchStudentById(int id) {
    Student student = repository.findStudentById(id);

    // 学生が見つからない場合は例外を投げる
    if (student == null) {
      throw new RuntimeException("受講生が見つかりません: " + id);
    }

    return student;

}
}