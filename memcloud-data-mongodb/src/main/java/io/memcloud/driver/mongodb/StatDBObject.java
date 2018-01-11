/**
 * 
 */
package io.memcloud.driver.mongodb;

import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author ganghuawang
 *
 */
public class StatDBObject implements DBObject{

	protected BasicDBObject doc = null;
	public StatDBObject() {
		doc = new BasicDBObject();
	}
	

	public BasicDBObject getDoc() {
		return doc;
	}


	@Override
	public boolean isPartialObject() {
		return doc.isPartialObject();
	}

	@Override
	public void markAsPartialObject() {
		doc.markAsPartialObject();
	}

	@Override
	public boolean containsField(String key) {
		return doc.containsField(key);
	}

	@Override
	public boolean containsKey(String key) {
		return doc.containsKey(key);
	}

	@Override
	public Object get(String key) {
		return doc.get(key);
	}

	@Override
	public Object put(String key, Object val) {
		return doc.put(key, val);
	}

	@Override
	public void putAll(BSONObject arg0) {
		doc.putAll(arg0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void putAll(Map arg0) {
		doc.putAll(arg0);
	}

	@Override
	public Object removeField(String key) {
		return doc.removeField(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map toMap() {
		return doc.toMap();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set keySet() {
		return doc.keySet();
	}

}
