package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer courseId;

  @NotNull(message = "受講生IDは必須です")
  private Integer studentId;

  @NotNull(message = "コース名は必須です")
  @Size(max = 100, message = "コース名は100文字以内で入力してください")
  private String courseName;

  // startday と endday はサーバー側で設定されるため、バリデーション不要
  private LocalDateTime startday;
  private LocalDateTime endday;

}
