package io.memcloud.memdns.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import com.github.downgoon.jresty.rest.model.UnifiedResponse;
import com.github.downgoon.jresty.rest.view.XStreamHandler;

public class MyUnifiedXmlHandler extends XStreamHandler {

	@Override
	protected XStream createXStream() {

		XStream xStream = new XStream();
		
		xStream.registerConverter( new com.thoughtworks.xstream.converters.Converter() {

			@SuppressWarnings("rawtypes")
			@Override
			public boolean canConvert(Class type) {
				return type.equals(Map.class) || type.equals(HashMap.class) || type.equals(LinkedHashMap.class);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
				Map map = (Map) source;
				for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
					Entry entry = (Entry) iterator.next();
					ExtendedHierarchicalStreamWriterHelper.startNode(writer, entry.getKey().toString(), Entry.class);
					if(entry.getValue() instanceof String) {
						writer.setValue((entry.getValue()!=null ? entry.getValue().toString() : ""));
					} else {
						writer.setValue((entry.getValue()!=null ? entry.getValue().toString() : ""));
					}
					writer.endNode();
				}
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0,
					UnmarshallingContext arg1) {
				return null;
			}
			
		});
		
		xStream.alias(UnifiedResponse.class.getSimpleName(),UnifiedResponse.class);
		return xStream;
	}

	
}
