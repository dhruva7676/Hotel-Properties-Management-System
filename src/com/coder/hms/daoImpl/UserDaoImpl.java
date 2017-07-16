package com.coder.hms.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.coder.hms.connection.DataSourceFactory;
import com.coder.hms.dao.UserDAO;
import com.coder.hms.entities.User;

public class UserDaoImpl implements UserDAO {

	private DataSourceFactory dataSourceFactory;
	private SessionFactory sessionFactory;
	private Session session;
	
	public UserDaoImpl() {
		
		dataSourceFactory = new DataSourceFactory();
		sessionFactory = dataSourceFactory.getSessionFactory();
		session = sessionFactory.getCurrentSession();
		session.beginTransaction();
	}
	
	@Override
	public User getUserByName(String theName) {
		Query<User> query = session.createQuery("from User where NickName=:theName", User.class);
		query.setParameter("theName", theName);
		User user = query.getSingleResult();
		return user;
	}

	@Override
	public void saveUser(User user) {
		session.saveOrUpdate(user);
		session.getTransaction().commit();

	}

	@Override
	public void changePasswordOfUser(long password) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> getAllusers() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean authentication(String userName, String userPswrd) {
		boolean isUser = false;
		Query<User> query = session.createQuery("from User where NickName=:userName and Password=:userPswrd", User.class);
		query.setParameter("userName", userName);
		query.setParameter("userPswrd", userPswrd);
		User user = query.getSingleResult();
		
		if(user != null) {
			isUser = true;
		}
		
		return isUser;
	}

}
