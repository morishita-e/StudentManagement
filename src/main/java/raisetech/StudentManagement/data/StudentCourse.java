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

  public void setStartday(LocalDateTime startday) {
    if (startday == null) {
      this.startday = LocalDateTime.now();
    } else {
      this.startday = startday;
    }
  }

  public void setEndday(LocalDateTime endday) {
    if (endday == null) {
      this.endday = LocalDateTime.now();
    } else {
      this.endday = endday;
    }
  }
}
