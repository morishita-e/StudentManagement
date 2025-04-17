package raisetech.StudentManagement.exceptionHandler;

/**
 * リソースの重複を表す汎用的な例外クラスです。
 * 例: メールアドレス、ユーザー名、学籍番号などの重複時に使用可能。
 */
public class DuplicateResourceException extends RuntimeException {

  private String fieldName;
  private String value;

  public String getFieldName() { return fieldName; }
  public String getValue() { return value; }

  public DuplicateResourceException(String message) {
    super(message);
  }

    public DuplicateResourceException(String fieldName, String value) {
      super(String.format("%s がすでに使用されています: %s", fieldName, value));
    }
  }
