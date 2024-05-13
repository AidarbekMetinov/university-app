package ua.com.foxminded.crm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.foxminded.validation.ValidDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmLecture {

	private Integer id;

	@NotNull(message = "is required")
	@Min(value = 1, message = "Choose course")
	private Integer courseId;

	@NotNull(message = "is required")
	@Min(value = 1, message = "Choose audience")
	private Integer audienceId;

	@NotNull(message = "is required")
	@Min(value = 1, message = "Choose teacher")
	private Integer teacherId;

	@NotNull(message = "is required")
	@ValidDateTime
	private String dateTime;
}