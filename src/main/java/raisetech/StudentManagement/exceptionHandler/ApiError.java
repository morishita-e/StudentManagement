package raisetech.StudentManagement.exceptionHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * APIエラーレスポンスの構造体です。
 * エラーの種類とメッセージを含み、クライアントに返す情報を管理します。
 */
@Getter
@Setter
@Schema(description = "APIエラー応答")
public class ApiError {

  @Schema(description = "エラーの種類（例：重複エラー、バリデーションエラーなど）", example = "重複エラー")
  private final String error;

  @Schema(description = "エラーメッセージの詳細", example = "メールアドレス test@example.com は既に登録されています。")
  private final String message;

  public ApiError(String error, String message) {
    this.error = error;
    this.message = message;
  }
}
