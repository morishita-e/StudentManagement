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

  @Schema(description = "コースID（自動採番）", example = "1")
  private Integer courseId;

  @Schema(description = "受講生ID (関連付けられた学生のID)", example = "1")
  private Integer studentId;

  @NotBlank(message = "コース名は必須です")
  @Size(max = 100, message = "コース名は100文字以内で入力してください")
  @Schema(description = "コース名", example = "Javaプログラミング入門")
  private String courseName;

  // startday と endday はサーバー側で設定されるため、バリデーション不要
  @Schema(description = "コースの開始日", example = "2025-04-01T09:00:00", pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startday;

  @Schema(description = "コースの終了日", example = "2025-06-01T17:00:00",  pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endday;
}
