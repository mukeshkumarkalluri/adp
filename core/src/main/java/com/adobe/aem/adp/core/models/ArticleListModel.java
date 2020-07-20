package com.adobe.aem.adp.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = { Resource.class })
public class ArticleListModel {
			
	@ValueMapValue(name = "parentPagePath", injectionStrategy = InjectionStrategy.OPTIONAL)
	private String parentPagePath;
	
	/** The resource. */
	@SlingObject
	private Resource resource;

	/** The resource resolver. */
	@SlingObject
	private ResourceResolver resourceResolver;

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(ArticleListModel.class);
	
	private List<Article> articlesList;

	/**
	 * Activate.
	 */
	@PostConstruct
	public void activate() {
		log.info("Inside Activate()"+parentPagePath);
		setArticlesList();
		log.info("End Activate()"+parentPagePath);
	}
	
	private void setArticlesList() {
		articlesList = new ArrayList<Article>();
		log.info("In setArticlesList() : "+ parentPagePath);
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page parentPage = null;
		if (pageManager != null) {
			parentPage = pageManager.getPage(parentPagePath);
		}
		Iterator<Page> itr = parentPage.listChildren();
		
		
		/*
		 * Display page title,
		 *  description, 
		 *  Url,
		 *  Image on a node: jcr:content/root/responsivegrid/image
		 *  And a date called article date on page properties Sort all the results by the article date
		 */
		
		//pagesMap.put(parentPage.getPageTitle(), FitbitLinkUtils.linkChecker(parentPage.getPath()));
		while (itr.hasNext()) {
			Page page = itr.next();
			log.info("Itr path :: {}", page.getName());
			String imagePath =null;
			
			if(page.hasChild("jcr:content/root/responsivegrid/image")) {
				log.info("{} has the image node ", page.getPath());
				log.info("image properties valuemap :{}",page.getProperties("jcr:content/root/responsivegrid/image"));
				
				try {
					Node pageNode = page.adaptTo(Node.class);
					if(pageNode.hasNode("jcr:content/root/responsivegrid/image")) {
						log.info("page has image:");
						Node imageNode = pageNode.getNode("jcr:content/root/responsivegrid/image");
						log.info("imageNode: {}", imageNode.getPath());
						imagePath = imageNode.hasProperty("fileReference") ? imageNode.getProperty("fileReference").getString() : null;
						log.info("image path :{}",imagePath);
					}
				} catch (RepositoryException e) {
					log.error("error while getting image info  :{}",e.getMessage());
				}
			}
			log.info("Article Date : {}",  page.getProperties().get("articleDate", Date.class) );
			
			Article article = new Article(page.getTitle(), page.getDescription(), page.getPath(),imagePath, page.getProperties().get("articleDate", Date.class) );
			articlesList.add(article);
			log.info("Article date : {}",article.getDate());
			log.info("Article Image : {}",article.getImagePath());
		}
	}

	/**
	 * This method is used to populate the pages Map.
	 *
	 * @param parentPage the parent page
	 */
	public List<Article> getArticlesList() {
		return Collections.unmodifiableList(articlesList);
	}

}
