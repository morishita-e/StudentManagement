package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生ID（自動採番）", example = "1")
  private Integer id;  //登録時はnull。更新時などに必要なら @NotNull

  @NotBlank(message = "名前は必須項目です")
  @Size(max = 100, message = "名前は100文字以内で入力してください")
  @Schema(description = "受講生の名前", example = "山田 太郎")
  private String name;

  @NotBlank(message = "ふりがなは必須です")
  @Size(max = 100, message = "ふりがなは100文字以内で入力してください")
  @Schema(description = "受講生のふりがな", example = "やまだ たろう")
  private String hurigana;

  @Size(max = 100, message = "ニックネームは100文字以内で入力してください")
  @Schema(description = "受講生のニックネーム", example = "たろちゃん")
  private String nicName;

  @NotBlank(message = "メールアドレスは必須です")
  @Email(message = "メールアドレスの形式が正しくありません")
  @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
  @Schema(description = "受講生のメールアドレス", example = "yamada@example.com")
  private String eMailAddress;

  @Size(max = 100, message = "住んでいる地域を100文字以内で入力してください")
  @Schema(description = "受講生の住んでいる地域", example = "東京")
  private String liveArea;

  @Min(value = 0, message = "年齢は0歳以上である必要があります")
  @Max(value = 120, message = "年齢は120歳以下である必要があります")
  @Schema(description = "受講生の年齢", example = "20")
  private Integer age;

  @Pattern(regexp = "^(男性|女性|その他)$", message = "性別は「男性」「女性」「その他」のいずれかで入力してください")
  @Schema(description = "受講生の性別", example = "男性")
  private String gender;

  @Size(max = 100, message = "備考は100文字以内で入力してください")
  @Schema(description = "受講生の備考", example = "連絡がつきにくい時間帯あり")
  private String remark;

  @Schema(description = "論理削除フラグ（削除済みでない場合はfalse）", example = "false")
  private Boolean isDeleted = false; // デフォルト値を false に設定
}