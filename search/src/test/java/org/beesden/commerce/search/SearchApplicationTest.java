package org.beesden.commerce.search;

import org.beesden.commerce.common.Utils;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith( SpringRunner.class )
//@SpringBootTest
//public class SearchApplicationTest {
//
//	@Test
//	public void runs() {
//	}
//
//}