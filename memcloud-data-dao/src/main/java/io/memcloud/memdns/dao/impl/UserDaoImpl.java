package io.memcloud.memdns.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import io.memcloud.memdns.dao.IUserDao;
import io.memcloud.memdns.dao.entry.User;

import com.github.downgoon.jresty.data.dao.IEntityDao;

	
public class UserDaoImpl implements IUserDao {
	@Resource(name = "entityDao")
	private IEntityDao<User, Long> entityDao;

	@Override
	public User get(Long id) {
		return entityDao.get(User.class, id);
	}

	@Override
	public Long save(User user) {
		return entityDao.save(user);
	}

	@Override
	public void update(User user) {
		entityDao.update(user);
	}

	@Override
	public void delete(Long id) {
		entityDao.delete(User.class, id);
	}

	@Override
	public User getByName(String name) {
		final Object[] conditions = new Object[] { name };
		final String hql = "select u from User u where u.name = ?";

		List<Object> idList = entityDao.execHQLList(hql, conditions, 1);
		if (idList == null || idList.size() == 0) {
			return null;
		}
		return (User)idList.get(0);
	}
}
