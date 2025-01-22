package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	Map<String,Integer> studentNamemap = new HashMap<String,Integer>();

	public StudentManagementApplication() {
		studentNamemap.put("Tanaka Tarou",16);
		studentNamemap.put("Suzuki Hanako",15);
		studentNamemap.put("Yamamoto Zirou",18);
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentNamemap")
	public Map<String,Integer> getStudentNamemap() {
		return studentNamemap;
	}

	@GetMapping("/List/name/{name}")
	public String getstudentName(@PathVariable String name) {
		if (studentNamemap.containsKey(name)) {
			return "Student Name: " + name + " Age: " + studentNamemap.get(name);
		} else {
			return "Student Name:" + name +  " not found ";
		}
	}

	@GetMapping("/List/age/{age}")
	public  Map<String, Integer> getstudentage(@PathVariable Integer age ) {
		Map<String, Integer> search = new HashMap<>();
		studentNamemap.forEach((name, studentAge) -> {
			if (studentAge.equals(age)) {
				search.put(name, studentAge);
			}
		});
		return search;
	}

@PostMapping("/update/{age}")
	public String updatestudent(@PathVariable Integer age,@RequestBody Map<String,String> student) {
		String ListName = student.get("ListName");
	String NewName = student.get("NewName");

	if(ListName == null || ListName.isEmpty()){
		return "Error No Date";
	}

	if(NewName == null || NewName.isEmpty()){
		return "Error No Date";
	}

	if( age <= 0){
		return  "Error No Date";
	}

	if (studentNamemap.containsKey(ListName)){
		studentNamemap.remove(ListName);
		studentNamemap.put(NewName,age);
		return "Update student name: " + ListName + " after Name: " + NewName + " with age: " + age;
	} else {
		studentNamemap.put(NewName,age);
		return "Added new student: " + NewName + " with age: " + age;
	}
}
}






