package ua.com.foxminded.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import ua.com.foxminded.exception.ConnectionException;

@Configuration
public class AppConfig {

	@Bean
	@Autowired
	public ScriptRunner scriptRunner(DataSource dataSource) {
		Connection connection;

		try {
			connection = dataSource.getConnection();
		} catch (SQLException exception) {
			throw new ConnectionException(exception);
		}

		return new ScriptRunner(connection);
	}

//	@Bean
//	public GroupedOpenApi groupOpenApi() {
//		String paths[] = { "/v1/**" };
//		String packagesToscan[] = { "test.org.springdoc.api.app68.api.user", "test.org.springdoc.api.app68.api.store" };
//		return GroupedOpenApi.builder().group("groups").pathsToMatch(paths).packagesToScan(packagesToscan).build();
//	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI().info(new Info().title("University API").version(appVersion)
				.description("This is a university api documentation"));
	}
}
