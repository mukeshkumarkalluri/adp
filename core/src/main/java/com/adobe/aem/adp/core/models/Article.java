package com.adobe.aem.adp.core.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.websocket.server.WebSocketHandler.Simple;

public class Article {
	
	/*
	 * Display page title,
	 *  description, 
	 *  Url,
	 *  Image on a node: jcr:content/root/responsivegrid/image
	 *  And a date called article date on page properties

Sort all the results by the article date
	 */
	String title;
	String description;
	String path;
	String imagePath;
	Date date;


	public Article(String title, String description, String path, String imagePath, Date date) {
		super();
		this.title = title;
		this.description = description;
		this.path = path;
		this.imagePath = imagePath;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Date getDate() {
		return date;
	}
	
	public String getDateasString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

}
