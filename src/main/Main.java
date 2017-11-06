package main;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.Comment;
import beans.MyPage;
import beans.Post;

public class Main {

	private static Scanner scanner;
	private static MyPage currentPage;
	private static List<MyPage> storedPages;
	private static List<Post> storedPosts = null;
	private static List<Comment> storedComments = null;
	private static List<Comment> storedReplies = null;

	public static void main(String[] args) throws SQLException {
		Facebook f = new Facebook();
		int op = -1;
		storedPages = new ArrayList<>();
		scanner = new Scanner(System.in);
		
		do {
			System.out.println(""
					+ "[1] Get Facebook ID by URL\n"
					+ "[2] Get posts by date\n"
					+ "[3] Get all comments\n"
					+ "[4] Get all replies\n"
					+ "[5] Get post's reactions\n"
					+ "[6] Get comment's reactions\n"
					+ "[7] Get reply's reactions\n"
					+ "[8] Load all stored pages\n"
					+ "[8] Get all pages"
					+ "Options:");
			
			op = scanner.nextInt();
			switch (op) {
				case 1:
					System.out.println("Informe uma URL: ");
					scanner = new Scanner(System.in);
					String url = "https://www.facebook.com/MichelTemer";//scanner.nextLine();
					
					System.out.println("url: " + url);
					Pattern regex = Pattern.compile("https:\\/\\/(.*)\\/(.*)");
					Matcher matcher = regex.matcher(url);
					
					if(matcher.matches()) {
						String name = matcher.group(2);
						System.out.println("Recuperando página de " + name);
						currentPage = f.getPageByName(name);
						if(currentPage == null) {
							System.out.println("Recovery error by name!");
						}
						else {
							System.out.println("Inform " + currentPage.getName() + "'s state");
							String state = ""; //scanner.nextLine();
							currentPage.setState(state);
							System.out.println("Inform " + currentPage.getName() + "'s party");
							String party = "";//scanner.nextLine();
							currentPage.setPoliticalParty(party);
							storedPages.add(currentPage);
						}
					}
					break;
				case 2:
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
					try {
						scanner = new Scanner(System.in);
						System.out.println("Enter initial date as format dd/mm/yy:");
						String initial = "01/10/17"; //scanner.nextLine()
						Date initialDate = new Date(format.parse(initial).getTime());
						System.out.println(">> " + initialDate.toString());
						
						scanner = new Scanner(System.in);
						System.out.println("Enter final date as format dd/mm/yy:");
						String fin = "05/11/17"; //scanner.nextLine()
						Date finalDate = new Date(format.parse(fin).getTime());
						System.out.println(">> " + finalDate.toString());
						
						System.out.println("Collecting data : " + currentPage.getName() + " page");
			            System.out.println("Starting collect");
						
			            storedPosts = f.storeIntervalPosts(currentPage, initialDate, finalDate);
						
						System.out.println("All posts from " + currentPage.getName() + " collected: " + storedPosts.size());
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				case 3:
					
					if(storedPosts == null) {
						System.out.println("Loading all posts...");
						storedPosts = Post.loadAllPosts();
					}
					System.out.println("Getting all comments...");
					f.getLastDateComment();
					storedComments = new ArrayList<>();
					
//					storedComments = f.storeOnlyComments(storedPosts.get(0), 1, null);
					for(Post post : storedPosts) {
						storedComments.addAll(f.storeOnlyComments(post, 1, null));
					}
					break;
				case 4:
					if(storedComments == null) {
						System.out.println("Loading all comments...");
						storedComments = Comment.loadAllComments(1);
					}
					System.out.println("Getting all replies... " + storedComments.size());
					f.getLastDateComment();
					
					storedReplies = new ArrayList<>();
					int i = 0;
					for(Comment c : storedComments) {
						System.out.println("Comment " + ++i);
						storedReplies.addAll(f.storeOnlyComments(c.getPost(), 2, c));
					}
					System.out.println(storedReplies.size() +  " replies retrievied...");
					break;
				case 5:
					if(storedPosts == null) {
						System.out.println("Loading all posts...");
						storedPosts = Post.loadAllPosts();
					}
					System.out.println("Getting all post's reactions...");
					
					//f.storeReactions(storedPosts.get(0).getPage().getIdFacebook(), storedPosts.get(0).getIdFacebook(), 1);
					for(Post post : storedPosts) {
						f.storeReactions(post.getPage().getIdFacebook(), post.getIdFacebook(), 1);
					}
					
					break;
				case 6:
					if(storedComments == null) {
						System.out.println("Loading all comments...");
						storedComments = Comment.loadAllComments(1);
					}
					System.out.println("Getting all comment's reactions... " + storedComments.size());
					int ii = 0;
					for(Comment comment : storedComments) {
						System.out.println("Comment " + ++ii);
						f.storeReactions(comment.getPost().getIdFacebook(), comment.getIdCommentFacebook(), 2);
					}
					break;
				case 7:
					if(storedReplies == null) {
						System.out.println("Loading all replies...");
						storedReplies = Comment.loadAllComments(2);
					}
					
					System.out.println("Getting all reply's reactions... " + storedReplies.size());
					
					for(Comment comment : storedReplies) {
						f.storeReactions(comment.getPost().getIdFacebook(), comment.getIdCommentFacebook(), 3);
					}
					break;
				case 8:
					storedPages.addAll(MyPage.getPages());
					break;
					
				case 9:
					if(storedPages.isEmpty()) {
						System.out.println("No pages in database, or pages not loaded. Feed it or choose option [7]!");
					} else {
						for (MyPage page : storedPages) {
							System.out.println("Collecting data : " + page.getName() + " page");
				            System.out.println("Starting collect");
				            
							f.storePosts(page, null, null);
							System.out.println("All posts from " + page.getName() + " collected");
						}
					}
					break;
	
				default:
					break;
			}
		} while(op != 10);

	}

}
