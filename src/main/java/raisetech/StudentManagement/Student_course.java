package raisetech.StudentManagement;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student_course {

  private int courseId;
  private Integer studentId;
  private String courseName;
  private LocalDateTime startday;
  private LocalDateTime endday;
}
