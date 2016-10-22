package org.beesden.search.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResult {

	private String id;
	private String title;
	private Map<String, String> metadata = new HashMap<>();

	public SearchResult( Document document ) {

		id = toSafe( document.getField( "id" ) );
		title = toSafe( document.getField( "title" ) );

		document.getFields().forEach( field -> metadata.put( field.name(), field.stringValue() ) );
	}

	private String toSafe( IndexableField type ) {
		if ( type == null ) {
			return null;
		}
		return type.stringValue();
	}

}