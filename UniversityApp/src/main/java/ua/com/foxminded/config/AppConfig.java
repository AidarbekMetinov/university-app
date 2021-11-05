package ua.com.foxminded.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
