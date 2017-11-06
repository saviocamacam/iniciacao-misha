package main;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.BatchUpdateException;

import org.apache.commons.lang3.ArrayUtils;
import org.postgresql.util.PSQLException;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookGraphException;
import com.restfb.json.JsonException;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;

import beans.Comment;
import beans.MyPage;
import beans.Post;
import beans.PostInfo;
import beans.Reaction;
import conexao.Conexao;

public class Facebook {

	private final String accessToken = "1339503919396928|461eb4d12ed790df16a6282a0379834d"; // i5XUUvsezTyGcAJe3N6tzpYvfOQ
	private final FacebookClient facebookClient;
	private Date lastDate = null;
	private List<PostInfo> listOfPost;

	public Facebook() {
		this.facebookClient = new DefaultFacebookClient(this.accessToken, Version.VERSION_2_9);
		new ArrayList<>();
		this.listOfPost = new ArrayList<>();
	}

	public void storePosts(MyPage page, Date initialDate, Date finalDate) throws SQLException {
		Connection<JsonObject> currentPage = facebookClient.fetchConnection(page.getIdFacebook() + "/feed", JsonObject.class);
		int i = 1;

		while (true) {
			Post post = null;
			for (JsonObject obj : currentPage.getData()) {
				try {
					post = Post.loadFromJson(obj, page);
					collectDataFromVisualization(post);

				} catch (JsonException exception) {
					System.out.println("[fb][collectVisualization] Exception");
					System.out.println("Minha exceçao" + obj.getString("id"));
					System.out.println(exception.getMessage());
				}
			}
			System.out.println("Page " + (i++));
			if (!post.getCreatedTime().substring(0, 4).equals("2017")) {
				break;
			}
			// Pega a próxima pagina e atualiza currentPage
			if (post.getCreatedTime().substring(0, 4).equals("2017"))
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
		}
	}

	public void storePosts2(MyPage page) throws SQLException {
		Connection<JsonObject> currentPage = facebookClient.fetchConnection(page.getIdFacebook() + "/feed", JsonObject.class);
		
		int i = 1;

		java.sql.Connection con = new Conexao().getConnection();
		System.out.println("getLastPost Open con");

		String sql = "select * from post order by pos_created_time asc limit 1";

		PreparedStatement stm = con.prepareStatement(sql);
		ResultSet rs = stm.executeQuery();
		rs.next();
		Date lastDate = stringToDate(rs.getString("POS_CREATED_TIME"));

		while (true) {
			Post post = null;
			for (JsonObject obj : currentPage.getData()) {
				try {
					post = Post.loadFromJson(obj, page);

					Date dateCurrentPost = stringToDate(post.getCreatedTime());
					if (lastDate.after(dateCurrentPost)) {
						// if (!post.isExists()) {
						System.out.println(post.getCreatedTime().substring(0, 4));
						if (post.getCreatedTime().substring(0, 4).equals("2017")
						/*
						 * || post.getCreatedTime().substring(0,
						 * 4).equals("2016") ||
						 * post.getCreatedTime().substring(0, 4).equals("2015")
						 */) {
							post.save();
						}
					}
				} catch (JsonException exception) {
					System.out.println("[fb][storePosts] Exception");
					System.out.println("Minha exceçao" + obj.getString("id"));
					System.out.println(exception.getMessage());
				}
			}
			System.out.println("Page " + (i++));
			if (!post.getCreatedTime().substring(0, 4).equals("2017")) {
				break;
			}
			// Pega a próxima pagina e atualiza currentPage
			if (post.getCreatedTime().substring(0, 4).equals("2017"))
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
		}
	}
	
	public List<Post> storeIntervalPosts(MyPage page, Date initialDate, Date finalDate) {
		Connection<JsonObject> currentPagination = facebookClient.fetchConnection(page.getIdFacebook() + "/feed", JsonObject.class);
		List<Post> storedPosts = new ArrayList<>();
		java.sql.Connection con = new Conexao().getConnection();
		String sql = "INSERT INTO Post (POS_ID_FACEBOOK,POS_MESSAGE, POS_STORY, POS_CREATED_TIME, POS_PAGE_ID )"
				+ "VALUES (?,?,?,?,?)";
        
        PreparedStatement statement;
        int i = 0;
		try {
			statement = con.prepareStatement(sql);
			while(true) {
				Post post = null;
				
				for (JsonObject obj : currentPagination.getData()) {
					post = Post.loadFromJson(obj, page);
					
					statement.setLong(1, post.getIdFacebook());
					statement.setString(2, post.getMessage());
					statement.setString(3, post.getStory());
					statement.setString(4, post.getCreatedTime());
					statement.setLong(5, page.getIdFacebook());
					
					Date createdTime = stringToDate(post.getCreatedTime());
					i++;
					
					if(createdTime.after(initialDate) && createdTime.before(finalDate)) {
						storedPosts.add(post);
						statement.addBatch();
					}
				}
				
				if(post != null) {
					Date createdTime = stringToDate(post.getCreatedTime());
					if(createdTime.after(finalDate)) {
						statement.executeBatch();
						break;
					}
				}
				
				if (!currentPagination.hasNext()) {
					statement.executeBatch();
					break;
				}
				if(i % 100 == 0) {
					statement.executeBatch();
					statement.close();
					statement = con.prepareStatement(sql);
				}
				
				currentPagination = facebookClient.fetchConnectionPage(currentPagination.getNextPageUrl(), JsonObject.class);
			}
			statement.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("================ {{{");
	        SQLException current = e;
	        do {
	            current.printStackTrace();
	        } while ((current = current.getNextException()) != null);
	        System.out.println("================ }}}");
	        
			e.printStackTrace();
		}
		return storedPosts;
	}

	private Date stringToDate(String string) {
		String lastPost = string;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
		Date lastDate = null;
		try {
			lastDate = new Date(format.parse(lastPost).getTime());
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return lastDate;
	}

	public void storeComments(Post post) throws SQLException {

		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(
					post.getPage().getIdFacebook() + "_" + post.getIdFacebook() + "/comments", JsonObject.class);

			while (true) {
				for (JsonObject obj : currentPage.getData()) {
					Comment comment = Comment.loadFromJson(obj, post);
					Date dateCurrentComment = stringToDate(comment.getCreatedTime());
					
					if (lastDate.after(dateCurrentComment)) {
						// if (!comment.isExists()) {
						comment.saveComment();
					} else
						System.out.println("Comment Skiped");
					storeReplies(comment, post);
				}
				if (!currentPage.hasNext()) {
					break;
				}
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][storeComments] " + e.getMessage());
		}
	}
	
	public List<Comment> storeOnlyComments(Post post, int mode, Comment c) throws SQLException {
		
		List<Comment> storedComments = new ArrayList<>();
		java.sql.Connection con = null;
		String sql = "INSERT INTO Comment ("
				+ "COM_ID_FACEBOOK, "
				+ "COM_ID_COMM_REPLIED, "
				+ "COM_MESSAGE, "
				+ "COM_FROM_ID, "
				+ "COM_FROM_NAME, "
				+ "COM_CREATED_TIME, "
				+ "COM_POST_ID) "
				+ "VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement statement = null;
        int i = 0;
        
        try {
        	Connection<JsonObject> currentPagination = null;
    		if(mode == 1)
    			currentPagination = facebookClient.fetchConnection(post.getPage().getIdFacebook() + "_" + post.getIdFacebook() + "/comments", JsonObject.class);
    		else {
    			currentPagination = facebookClient.fetchConnection(
    					c.getPost().getIdFacebook() + "_" + c.getIdCommentFacebook() + "/comments",
    					JsonObject.class);
    		}
    		
			
			while(true) {
				if(currentPagination.getData().size() > 0) {
					con = new Conexao().getConnection();
					statement = con.prepareStatement(sql);
				}
				Comment comment = null;
				
				for (JsonObject obj : currentPagination.getData()) {
					if(mode == 1)
						comment = Comment.loadFromJson(obj, post);
					else {
						comment = Comment.loadFromJson(obj, post, c);
					}
					
					statement.setLong(1, comment.getIdCommentFacebook());
					statement.setLong(2, comment.getIdRepliedComment());
					statement.setString(3, comment.getMessage());
					statement.setLong(4, comment.getFromId());
					statement.setString(5, comment.getFromName());
					statement.setString(6, comment.getCreatedTime());
					statement.setLong(7, post.getIdFacebook());
					
					Date createdTime = stringToDate(post.getCreatedTime());
					
					i++;
					if(mode == 1 && lastDate == null) {
						
						storedComments.add(comment);
						statement.addBatch();
					}
					else if(mode == 1 && createdTime.after(lastDate))
						break;
					else {
						storedComments.add(comment);
						statement.addBatch();
					}
				}
				if (!currentPagination.hasNext()) {
					if(statement!= null)
						statement.executeBatch();
					break;
				}
				if(statement != null && i % 1000 == 0) {
					statement.executeBatch();
					statement.close();
					statement = con.prepareStatement(sql);
				}
				
				currentPagination = facebookClient.fetchConnectionPage(currentPagination.getNextPageUrl(), JsonObject.class);
			}
			
		} catch (SQLException e) {
			
			System.out.println("================ {{{");
	        SQLException current = e;
	        do {
	        	System.out.println("[error] " + e.getMessage() + " error type: " + e.getErrorCode() + " error cause: " + e.getCause());
	            //current.printStackTrace();
	        } while ((current = current.getNextException()) != null);
	        System.out.println("================ }}}");
			//e.printStackTrace();
		} catch(FacebookGraphException g) {
			System.out.println("[comment][saveLikes] "+ g.getMessage());
			System.out.println("[typeError] " + g.getErrorType() + " Error code: " + g.getErrorCode());
			
		} finally {
			if(con != null)
				con.close();
			if(statement != null)
				statement.close();
		}
        System.out.println("Retrieved comments: " + storedComments.size());
        return storedComments;
	}
	
	public List<Comment> storeReplies(Comment c) {
		Connection<JsonObject> replies = facebookClient.fetchConnection(
				c.getPost().getIdFacebook() + "_" + c.getIdCommentFacebook() + "/comments",
				JsonObject.class);
		List<Comment> storedReplies = new ArrayList<>();
		java.sql.Connection con = new Conexao().getConnection();
		String sql = "INSERT INTO Comment ("
				+ "COM_ID_FACEBOOK, "
				+ "COM_ID_COMM_REPLIED, "
				+ "COM_MESSAGE, "
				+ "COM_FROM_ID, "
				+ "COM_FROM_NAME, "
				+ "COM_CREATED_TIME, "
				+ "COM_POST_ID) "
				+ "VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement statement = null;
        int i = 0;
        
        try {
			statement = con.prepareStatement(sql);
			
			while(true) {
				Comment reply = null;
				for(JsonObject obj: replies.getData()) {
					reply = Comment.loadFromJson(obj, c.getPost(), c);
					
					statement.setLong(1, reply.getIdCommentFacebook());
					statement.setLong(2, reply.getIdRepliedComment());
					statement.setString(3, reply.getMessage());
					statement.setLong(4, reply.getFromId());
					statement.setString(5, reply.getFromName());
					statement.setString(6, reply.getCreatedTime());
					statement.setLong(7, c.getPost().getIdFacebook());
					
					Date createdTime = stringToDate(reply.getCreatedTime());
					i++;
					
					if(lastDate == null) {
						storedReplies.add(reply);
						statement.addBatch();
					}
					else if(createdTime.after(lastDate))
						break;
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return storedReplies;
	}
	
	public void storeReplies(Comment comment, Post post) throws SQLException {
		try {
			Connection<JsonObject> replies = facebookClient.fetchConnection(
					comment.getPost().getIdFacebook() + "_" + comment.getIdCommentFacebook() + "/comments",
					JsonObject.class);
			while (true) {
				for (JsonObject obj2 : replies.getData()) {
					Comment reply = Comment.loadFromJson(obj2, post, comment);
					Date dateCurrentComment = stringToDate(reply.getCreatedTime());
					if (lastDate.after(dateCurrentComment)) {
						// if (!comment.isExists()) {
						reply.saveComment();
					} else
						System.out.println("Reply Skiped");
				}
				if (!replies.hasNext()) {
					break;
				}
				replies = facebookClient.fetchConnectionPage(replies.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][storeReplies] " + e.getMessage());
		}
	}

	public float[] countComments(Post post) {
		float[] reactionsComments = new float[6];
		float[] reactionsReplies = new float[7];

		float comments = 0;
		float replies = 0;

		float meanCommentLike = 0;
		float meanCommentLove = 0;
		float meanCommentHaha = 0;
		float meanCommentWow = 0;
		float meanCommentSad = 0;
		float meanCommentAngry = 0;

		float meanReplyLike = 0;
		float meanReplyLove = 0;
		float meanReplyHaha = 0;
		float meanReplyWow = 0;
		float meanReplySad = 0;
		float meanReplyAngry = 0;

		int i = 0;
		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(
					post.getPage().getIdFacebook() + "_" + post.getIdFacebook() + "/comments", JsonObject.class);

			while (true) {
				comments += currentPage.getData().size();
				System.out.println("com: " + comments);
				i++;
				for (JsonObject obj : currentPage.getData()) {
					Comment comment = Comment.loadFromJson(obj, post);
					reactionsComments = countReactions(post.getIdFacebook(), comment.getIdCommentFacebook());

					meanCommentLike += reactionsComments[0];
					meanCommentLove += reactionsComments[1];
					meanCommentWow += reactionsComments[3];
					meanCommentHaha += reactionsComments[4];
					meanCommentSad += reactionsComments[5];
					meanCommentAngry += reactionsComments[6];

					reactionsReplies = countReplies(comment, post);

					replies += reactionsReplies[0];
					meanReplyLike += reactionsReplies[1];
					meanReplyLove += reactionsReplies[2];
					meanReplyWow += reactionsReplies[3];
					meanReplyHaha += reactionsReplies[4];
					meanReplySad += reactionsReplies[5];
					meanReplyAngry += reactionsReplies[6];
				}

				if (!currentPage.hasNext()) {
					break;
				}
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][countComments] " + e.getMessage());
		}
		float[] both = new float[14];

		both[0] = comments;
		both[1] = replies;
		both[2] = meanCommentLike / comments;
		both[3] = meanCommentLove / comments;
		both[4] = meanCommentHaha / comments;
		both[5] = meanCommentWow / comments;
		both[6] = meanCommentSad / comments;
		both[7] = meanCommentAngry / comments;
		both[8] = meanReplyLike / replies;
		both[9] = meanReplyLove / replies;
		both[10] = meanReplyHaha / replies;
		both[11] = meanReplyWow / replies;
		both[12] = meanReplySad / replies;
		both[13] = meanReplyAngry / replies;

		System.out.print("POST " + post.getIdFacebook() + " -- CREATED " + post.getCreatedTime() + " -- COMMENTS "
				+ both[0] + " -- REPLIES " + both[1] + " -- MEAN_COM_LIKE " + both[2] + " -- MEAN_COM_LOVE " + both[3]
				+ " -- MEAN_COM_HAHA " + both[4] + " -- MEAN_COM_WOW " + both[5] + " -- MEAN_COM_SAD " + both[6]
				+ " -- MEAN_COM_ANGRY " + both[7] + " -- MEAN_REP_LIKE " + both[8] + " -- MEAN_REP_LOVE " + both[9]
				+ " -- MEAN_REP_HAHA " + both[10] + " -- MEAN_REP_WOW " + both[11] + " -- MEAN_REP_SAD " + both[12]
				+ " -- MEAN_REP_ANGRY " + both[13]);
		return both;
	}

	private float[] countReplies(Comment comment1, Post post) {
		float[] numberOfReplies = new float[1];
		float[] reactionsReplies = new float[6];
		float like = 0;
		float love = 0;
		float haha = 0;
		float wow = 0;
		float sad = 0;
		float angry = 0;
		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(
					comment1.getPost().getIdFacebook() + "_" + comment1.getIdCommentFacebook() + "/comments",
					JsonObject.class);

			while (true) {
				numberOfReplies[0] += currentPage.getData().size();

				for (JsonObject obj : currentPage.getData()) {
					Comment comment = Comment.loadFromJson(obj, post);
					reactionsReplies = countReactions(post.getIdFacebook(), comment.getIdCommentFacebook());
					like += reactionsReplies[0];
					love += reactionsReplies[1];
					haha += reactionsReplies[2];
					wow += reactionsReplies[3];
					sad += reactionsReplies[4];
					angry += reactionsReplies[5];
				}
				if (!currentPage.hasNext()) {
					break;
				}
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][countReplies] " + e.getMessage());
		}
		reactionsReplies[0] = like;
		reactionsReplies[1] = love;
		reactionsReplies[2] = haha;
		reactionsReplies[3] = wow;
		reactionsReplies[4] = sad;
		reactionsReplies[5] = angry;

		float[] both = (float[]) ArrayUtils.addAll(numberOfReplies, reactionsReplies);

		return both;
	}

	private int countShares(Post post) {
		int shares = 0;
		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(post.getPage().getIdFacebook() + "_"
					+ post.getIdFacebook() + "/?fields=shares&access_token=" + accessToken, JsonObject.class);
			JsonObject object;

			if (!currentPage.getData().isEmpty()) {
				object = currentPage.getData().get(0);
				JsonObject obj2 = object.getJsonObject("shares");
				shares = obj2.getInt("count");
			}
		} catch (Exception e) {
			System.out.println("[fb][countShares] " + e.getMessage());
		}
		System.out.println("Shares: " + post.getIdFacebook() + "/" + shares);
		return shares;
	}

	// "/?fields=reactions.type(" + reaction +
	// ").limit(0).summary(1)&access_token="+
	private float[] countReactions(Long idFacebook, Long idObject) {
		float[] reactions = { 0, 0, 0, 0, 0, 0 };
		String query;
		if (idObject != 0L)
			query = idFacebook + "_" + idObject;
		else
			query = idFacebook + "";
		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(query + "/reactions", JsonObject.class);

			JsonObject object;

			while (true) {
				for (JsonObject obj : currentPage.getData()) {
					String type = obj.getString("type");
					switch (type) {
					case "LIKE":
						reactions[0] += 1;
						break;
					case "LOVE":
						reactions[1] += 1;
						break;
					case "HAHA":
						reactions[2] += 1;
						break;
					case "WOW":
						reactions[3] += 1;
						break;
					case "SAD":
						reactions[4] += 1;
						break;
					case "ANGRY":
						reactions[5] += 1;
						break;

					default:
						System.out.println();
					}
				}
				if (!currentPage.hasNext()) {
					break;
				}
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
			}

			/*
			 * if (!currentPage.getData().isEmpty()) { object =
			 * currentPage.getData().get(0); JsonObject obj2 =
			 * object.getJsonObject("summary"); reactions =
			 * obj2.getInt("total_count"); }
			 */

		} catch (Exception e) {
			System.out.println("[fb][countReactions] " + e.getMessage());
		}

		return reactions;
	}	

	public void storePostReaction(Post post) {
		try {
			Connection<JsonObject> reactionsPost = facebookClient.fetchConnection(
					post.getPage().getIdFacebook() + "_" + post.getIdFacebook() + "/reactions", JsonObject.class);

			while (true) {
				for (JsonObject obj2 : reactionsPost.getData()) {
					if (!post.hasReaction(obj2)) {
						post.saveReactionPost(obj2);
					}
				}
				if (!reactionsPost.hasNext()) {
					break;
				}
				reactionsPost = facebookClient.fetchConnectionPage(reactionsPost.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][storePostReaction] " + e.getMessage());
		}
	}
	
	public void storeReactions(Long idFacebook, Long idObject, int mode) throws SQLException {
		java.sql.Connection con = null;
		String query = idFacebook + "_" + idObject;
		
		String sql = "";
		
		if (mode == 1)
			sql = "INSERT INTO Reaction_Post(RPT_ID_USER, RPT_ID_POST, RPT_TYPE) VALUES (?,?,?)";
		else if (mode == 2)
			sql = "INSERT INTO Reaction_Comment(RCT_ID_USER, RCT_ID_COMMENT, RCT_TYPE) VALUES (?,?,?)";
		else if (mode == 3)
			sql = "INSERT INTO Reaction_Reply(RRP_ID_USER, RRP_ID_REPLY, RRP_TYPE) VALUES (?,?,?)";
		
		int i = 0;
		List<Reaction> listReactions = new ArrayList<>();
		PreparedStatement statement = null;
		try {
			Connection<JsonObject> currentPage = facebookClient.fetchConnection(query + "/reactions", JsonObject.class);
			
			if(currentPage.getData().size() > 0) {
				con = new Conexao().getConnection();
				statement = con.prepareStatement(sql);
			}
			
			while (true) {
				Reaction reaction = null;
				
				for (JsonObject obj2 : currentPage.getData()) {
					reaction = Reaction.loadFromJson(obj2);
					reaction.setIdObject(idObject);
		            
		            statement.setLong(1, reaction.getIdUser());
		            statement.setLong(2, idObject);
		            statement.setString(3, reaction.getType());
		            
		            i++;
		            statement.addBatch();
					listReactions.add(reaction);
				}
				if(statement != null && i % 1000 == 0) {
					statement.executeBatch();
					statement.close();
					statement = con.prepareStatement(sql);
				}
				
				if (!currentPage.hasNext()) {
					if(statement != null)
						statement.executeBatch();
					break;
				}
				currentPage = facebookClient.fetchConnectionPage(currentPage.getNextPageUrl(), JsonObject.class);
			}
		} catch (Exception e) {
			System.out.println("[fb][storePostReaction] " + e.getMessage());
		} finally {
			if(con != null)
				con.close();
			if(statement != null)
				statement.close();
		}
		System.out.println("Comments's Reactions count: " + listReactions.size());
	}

	public void getLastDateComment() throws SQLException {
		java.sql.Connection con = new Conexao().getConnection();
		System.out.println("getLastReply Open con");
		String sql = "select * from comment order by com_created_time asc limit 1";

		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			
			while(rs.next())
				this.lastDate = stringToDate(rs.getString("COM_CREATED_TIME"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
			System.out.println("saveComment Close con");
		}
	}

	public void collectDataFromVisualization(Post post) {
		PostInfo newPost = new PostInfo(post.getIdFacebook());
		// newPost.setShares(countShares(post));

		float[] commentReply = countComments(post);
		System.out.println("ID POST: " + post.getIdFacebook());
		long a = 0L;
		float[] reactions = countReactions(post.getIdFacebook(), a);

		newPost.setPostLike(reactions[0]);
		newPost.setPostLove(reactions[1]);
		newPost.setPostHaha(reactions[2]);
		newPost.setPostWow(reactions[3]);
		newPost.setPostSad(reactions[4]);
		newPost.setPostAngry(reactions[5]);

		newPost.setComments(commentReply[0]);
		newPost.setReplies(commentReply[1]);
		newPost.setMeanCommentLike(commentReply[2]);
		newPost.setMeanCommentLove(commentReply[3]);
		newPost.setMeanCommentHaha(commentReply[4]);
		newPost.setMeanCommentWow(commentReply[5]);
		newPost.setMeanCommentSad(commentReply[6]);
		newPost.setMeanCommentAngry(commentReply[7]);
		newPost.setMeanReplyLike(commentReply[8]);
		newPost.setMeanReplyLove(commentReply[9]);
		newPost.setMeanReplyHaha(commentReply[10]);
		newPost.setMeanReplyWow(commentReply[11]);
		newPost.setMeanReplySad(commentReply[12]);
		newPost.setMeanReplyAngry(commentReply[13]);
		this.listOfPost.add(newPost);

		System.out.println(" -- LIKE " + reactions[0] + " -- LOVE " + reactions[1] + " -- HAHA " + reactions[2] + " -- WOW "
				+ reactions[3] + " -- SAD " + reactions[4] + " -- ANGRY " + reactions[5]);

		// this.listOfPost.sort(c);
	}

	public MyPage getPageByName(String name) {
		System.out.println("name: " + name);
		Page currentPage = facebookClient.fetchObject(name, Page.class);
		
		MyPage page = new MyPage();
		page.setIdFacebook(Long.parseLong(currentPage.getId()));
		page.setName(currentPage.getName());
		
		return page;
	}

}
