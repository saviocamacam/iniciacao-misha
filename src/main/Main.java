package main;

import java.sql.SQLException;
import java.util.List;

import beans.Page;
import beans.Post;

public class Main {

	public static void main(String[] args) throws SQLException {
		Facebook f = new Facebook();
		List<Page> pages = Page.getPages();
		
		for (Page page : pages) {
			System.out.println("Collecting data : " + page.getName() + " page");
            System.out.println("Starting collect");
            
			f.storePosts(page);
			System.out.println("All posts from " + page.getName() + " collected");
		}

	}

}
