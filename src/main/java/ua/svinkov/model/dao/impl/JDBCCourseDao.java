package ua.svinkov.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.svinkov.constants.SqlConstants;
import ua.svinkov.model.dao.CourseDao;
import ua.svinkov.model.dao.mapper.CourseMapper;
import ua.svinkov.model.entity.Course;

public class JDBCCourseDao implements CourseDao {
	private Connection connection;

	public JDBCCourseDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Course entity) {
		try (PreparedStatement pstmt = connection.prepareStatement(SqlConstants.INSERT_COURSE)) {

			pstmt.setString(1, entity.getCourse());
			pstmt.setInt(2, entity.getTopic().getTopicId());
			pstmt.setInt(3, entity.getTeacher().getUserid());
			pstmt.setDate(4, java.sql.Date.valueOf(entity.getDateStart()));
			pstmt.setDate(5, java.sql.Date.valueOf(entity.getDateEnd()));
			pstmt.setString(6, entity.getDescription());
			pstmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Course findById(int id) {
		Course course = null;
		ResultSet rs = null;
		try (PreparedStatement st = connection.prepareStatement(SqlConstants.FIND_COURSE_BY_ID)) {
			CourseMapper mapper = new CourseMapper();
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next())
				course = mapper.extractFromResultSet(rs);
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return course;
	}

	@Override
	public List<Course> findAll() {
		List<Course> course = new ArrayList<>();
		ResultSet rs = null;
		try (PreparedStatement st = connection.prepareStatement(SqlConstants.FIND_ALL_COURSE)) {
			CourseMapper mapper = new CourseMapper();
			rs = st.executeQuery();
			while (rs.next())
				course.add(mapper.extractFromResultSet(rs));
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return course;
	}

	@Override
	public void update(Course entity) {
        try (PreparedStatement st = connection.prepareStatement(SqlConstants.UPDATE_COURSE)) {
            st.setString(1, entity.getCourse());
            st.setInt(2, entity.getTopic().getTopicId());
            st.setInt(3, entity.getTeacher().getUserid());
            st.setDate(4, java.sql.Date.valueOf(entity.getDateStart()));
            st.setDate(5, java.sql.Date.valueOf(entity.getDateEnd()));
            st.setString(6, entity.getDescription());
            st.setInt(7, entity.getCourseid());
            st.executeUpdate();
        } catch (SQLException ex) {
        	
            ex.printStackTrace();
        }
	}

	@Override
	public void delete(int id) {
		try (PreparedStatement pstmt = connection.prepareStatement(SqlConstants.DELETE_COURSE)) {
			pstmt.setInt(1, id);
			pstmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Integer> findCoursesIdSortBySt(int teacherid){
		List<Integer> course = new ArrayList<>();
		ResultSet rs = null;
		try (PreparedStatement st = connection.prepareStatement(SqlConstants.FIND_COURSE_BY_ID_SORTED)) {
			st.setInt(1, teacherid);
			rs = st.executeQuery();
			while (rs.next())
				course.add(rs.getInt("courseid"));
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return course;
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setAutoCommit(boolean commit) {
		try {
			connection.setAutoCommit(commit);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rollBackAndClose() {
		try {
			connection.rollback();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commit() {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
