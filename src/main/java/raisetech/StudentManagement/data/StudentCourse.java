package raisetech.StudentManagement.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer courseId;
  private Integer studentId;
  private String courseName;
  private LocalDateTime startday;
  private LocalDateTime endday;

}
