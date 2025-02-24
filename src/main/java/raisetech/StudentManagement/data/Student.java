package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer id;
  private String name;
  private String hurigana;
  private String nicName;
  private String eMailAddress;
  private String liveArea;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}

