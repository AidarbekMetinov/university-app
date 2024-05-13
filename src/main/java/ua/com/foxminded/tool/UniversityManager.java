package ua.com.foxminded.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UniversityManager {

	ScriptManager scriptManager;
	private final String REMOVE_TABLES_SQL;
	private final String FACULTY_SQL;
	private final String TEACHER_SQL;
	private final String FACULTY_TEACHER_SQL;
	private final String COURSE_SQL;
	private final String COURSE_TEACHER_SQL;
	private final String AUDIENCE_SQL;
	private final String GROUP_SQL;
	private final String STUDENT_SQL;
	private final String LECTURE_SQL;
	private final String GROUP_LECTURE_SQL;

	@Autowired
	public UniversityManager(ScriptManager scriptManager, Environment env) {
		this.scriptManager = scriptManager;
		REMOVE_TABLES_SQL = env.getProperty("sql.remove.tables");
		FACULTY_SQL = env.getProperty("sql.faculty");
		TEACHER_SQL = env.getProperty("sql.teacher");
		FACULTY_TEACHER_SQL = env.getProperty("sql.faculty.teacher");
		COURSE_SQL = env.getProperty("sql.course");
		COURSE_TEACHER_SQL = env.getProperty("sql.course.teacher");
		AUDIENCE_SQL = env.getProperty("sql.audience");
		GROUP_SQL = env.getProperty("sql.group");
		STUDENT_SQL = env.getProperty("sql.student");
		LECTURE_SQL = env.getProperty("sql.lecture");
		GROUP_LECTURE_SQL = env.getProperty("sql.group.lecture");
	}

	public void runSqlScripts() {
		scriptManager.executeScript(REMOVE_TABLES_SQL);
		scriptManager.executeScript(FACULTY_SQL);
		scriptManager.executeScript(TEACHER_SQL);
		scriptManager.executeScript(FACULTY_TEACHER_SQL);
		scriptManager.executeScript(COURSE_SQL);
		scriptManager.executeScript(COURSE_TEACHER_SQL);
		scriptManager.executeScript(AUDIENCE_SQL);
		scriptManager.executeScript(GROUP_SQL);
		scriptManager.executeScript(STUDENT_SQL);
		scriptManager.executeScript(LECTURE_SQL);
		scriptManager.executeScript(GROUP_LECTURE_SQL);
	}
}