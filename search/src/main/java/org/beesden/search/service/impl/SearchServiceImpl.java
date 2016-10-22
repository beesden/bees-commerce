package org.beesden.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.beesden.common.model.SearchDocument;
import org.beesden.search.exception.SearchIndexingException;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResult;
import org.beesden.search.model.SearchResultEntity;
import org.beesden.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

	private Analyzer analyzer = new StandardAnalyzer();
	private Directory index;

	@Autowired
	public SearchServiceImpl( Directory index ) {
		this.index = index;
	}

	@Override
	public SearchResult performSearch( SearchForm searchForm ) {

		MultiFieldQueryParser queryParser = new MultiFieldQueryParser( new String[]{ "title" }, analyzer );
		queryParser.setAllowLeadingWildcard( true );

		Query query;
		try {
			query = queryParser.parse( searchForm.getTitle() );
		} catch ( ParseException e ) {
			// todo - rethrow
			e.printStackTrace();
			return null;
		}

		IndexReader reader;
		try {
			reader = DirectoryReader.open( index );
		} catch ( IOException e ) {
			// todo - rethrow
			e.printStackTrace();
			return null;
		}

		IndexSearcher searcher = new IndexSearcher( reader );
		TopScoreDocCollector collector = TopScoreDocCollector.create( 10 );

		try {
			searcher.search( query, collector );
		} catch ( IOException e ) {
			// todo - rethrow
			e.printStackTrace();
			return null;
		}

		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		SearchResult searchResult = new SearchResult();

		for ( ScoreDoc hit : hits ) {
			SearchResultEntity entity = new SearchResultEntity();
			int docId = hit.doc;

			Document document;
			try {

				document = searcher.doc( docId );
				entity.setTitle( toSafe( document.getField( "title" ) ) );

			} catch ( IOException e ) {
				// todo - throw error?
				e.printStackTrace();
			}

			searchResult.addEntity( entity );
		}
		return searchResult;
	}

	@Override
	public void removeFromIndex( SearchDocument searchDocument ) {

	}

	@Override
	public void submitToIndex( SearchDocument searchDocument ) {

		try {
			IndexWriterConfig config = new IndexWriterConfig( analyzer );
			IndexWriter writer = new IndexWriter( index, config );

			Document document = new Document();

			document.add( new TextField( "title", searchDocument.getTitle(), Field.Store.YES ) );
			document.add( new TextField( "type", searchDocument.getEntityType().name(), Field.Store.YES ) );

			writer.addDocument( document );
			writer.close();

		} catch ( IOException e ) {
			throw new SearchIndexingException( searchDocument );
		}


	}

	private String toSafe( IndexableField type ) {
		if ( type == null ) {
			return null;
		}
		return type.stringValue();
	}


}


