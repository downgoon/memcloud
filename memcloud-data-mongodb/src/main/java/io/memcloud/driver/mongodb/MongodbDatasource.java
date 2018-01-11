package io.memcloud.driver.mongodb;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongodbDatasource {

	private DB mongoDB;

	public MongodbDatasource(String host, int port, String dbname) {
		this(host, port, dbname, null, null);
	}

	/**
	 * http://api.mongodb.org/java/2.7.3/com/mongodb/MongoOptions.html
	 */
	public MongodbDatasource(String host, int port, String dbname, String user, String pwd) {
		try {
			ServerAddress addr = new ServerAddress(host, port);
			MongoOptions options = new MongoOptions();
			Mongo mongo = new Mongo(addr, options);
			mongoDB = mongo.getDB(dbname);
			if (user != null && pwd != null && user.trim().length() > 0 && pwd.trim().length() > 0) {
				boolean authed = mongoDB.authenticate(user, pwd.toCharArray());
				if (!authed) {
					throw new IllegalArgumentException("Mongodb User Or passowrd Wrong");
				}
			}

		} catch (UnknownHostException e) {
			throw new IllegalStateException("unkown host: " + host, e);
		}
	}

	public DB getMongoDB() {
		return mongoDB;
	}

	public DBCollection getDBCollection(String collName) {
		return mongoDB.getCollection(collName);
	}

}
