package ua.com.foxminded.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ua.com.foxminded.exception.NotFoundException;

@Component
public class ScriptManager {

	@Autowired
	private ScriptRunner scriptRunner;

	private String sqlBasePath;

	@Autowired
	public ScriptManager(Environment env) {
		this.sqlBasePath = env.getProperty("sql.basepath");
	}

	/**
	 * @param scriptFileName file name. Example: init.sql
	 * @throws FileNotFoundException
	 * @apiNote Executes script in a SQL file.
	 */

	public void executeScript(String scriptFileName) {
		try (BufferedReader reader = new BufferedReader(new FileReader(sqlBasePath + scriptFileName))) {
			scriptRunner.runScript(reader);
		} catch (IOException e) {
			throw new NotFoundException(e);
		}
	}
}