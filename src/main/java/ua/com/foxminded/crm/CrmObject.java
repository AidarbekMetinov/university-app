package ua.com.foxminded.crm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmObject {

	@NotNull(message = "is required")
	@Min(value = 1, message = "is required")
	private Integer id;
}