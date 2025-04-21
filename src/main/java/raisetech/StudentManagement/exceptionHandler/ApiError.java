package raisetech.StudentManagement.exceptionHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * APIエラーレスポンスの構造体です。
 * エラーの種類とメッセージを含み、クライアントに返す情報を管理します。
 */
@Getter
@Setter
public class ApiError {
  private final String error;
  private final String message;

  public ApiError(String error, String message) {
    this.error = error;
    this.message = message;
  }
}
