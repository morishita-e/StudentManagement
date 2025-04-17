package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private Integer courseId;

  private Integer studentId;

  @NotBlank(message = "コース名は必須です")
  @Size(max = 100, message = "コース名は100文字以内で入力してください")
  private String courseName;

  // startday と endday はサーバー側で設定されるため、バリデーション不要
  private LocalDateTime startday;
  private LocalDateTime endday;

}
