package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.restfb.json.JsonObject;

import conexao.Conexao;

public class Post {

	private MyPage page;

	private Long idFacebook; // POS_ID_FACEBOOK
	private String createdTime; // POS_CREATED_TIME
	private String message; // POS_MESSAGE
	private String story; // POS_STORY

	private ArrayList<Reaction> reactionsList;
	private ArrayList<Comment> commentsList;

	public Post() {

	}

	public static Post loadFromJson(JsonObject obj, MyPage page) {
		Post post = new Post();

		post.setIdFacebook(Long.parseLong(obj.getString("id").split("_")[1]));
		post.setCreatedTime(obj.getString("created_time"));
		if (!obj.isNull("message")) {
			post.setMessage(obj.getString("message"));
		}
		if (!obj.isNull("story")) {
			post.setStory(obj.getString("story"));
		}
		post.setPage(page);
		return post;
	}

	@SuppressWarnings("finally")
	public boolean isExists() throws SQLException {
		Connection con = new Conexao().getConnection();
		System.out.println("Post isExists Open con");
		boolean result = false;
		try {
			String sql = "SELECT * FROM POST WHERE POS_ID_FACEBOOK = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, this.getIdFacebook());
			ResultSet rs = stm.executeQuery();
			result = rs.next();
			rs.close();
			stm.close();
		} catch (SQLException ex) {
			System.out.println("[post][isExists] " + ex.getMessage());
		} finally {
			con.close();
			System.out.println("Post isExists Close con");
			return result;
		}
	}

	public void save() throws SQLException {
		Connection con = new Conexao().getConnection();
		System.out.println("save Open con");
		try {
			String sql = "INSERT INTO Post (POS_ID_FACEBOOK,POS_MESSAGE, POS_STORY, POS_CREATED_TIME, POS_PAGE_ID )"
					+ "VALUES (?,?,?,?,?)";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, this.idFacebook);
			stm.setString(2, this.message);
			stm.setString(3, this.story);
			stm.setString(4, this.createdTime);
			stm.setLong(5, this.getPage().getIdFacebook());
			stm.executeUpdate();
			stm.close();
		} catch (SQLException ex) {
			System.out.println("[post][save] " + ex.getMessage());
		} finally {
			con.close();
			System.out.println("save Close con");
		}
	}
	
	public static List<Post> getPostsByPage(MyPage page) throws SQLException {
        List<Post> posts = new ArrayList<>();
        Connection con = new Conexao().getConnection();
        System.out.println("getPostByPage Open con");
        String sql = "SELECT POS_ID_FACEBOOK,POS_MESSAGE,POS_STORY, POS_CREATED_TIME, POS_PAGE_ID "
                + "FROM POST WHERE POS_PAGE_ID = ?";

        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, page.getIdFacebook());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setIdFacebook(rs.getLong("POS_ID_FACEBOOK"));
                post.setMessage(rs.getString("POS_MESSAGE"));
                post.setStory(rs.getString("POS_STORY"));
                post.setCreatedTime(rs.getString("POS_CREATED_TIME"));
                post.setPage(page);
                posts.add(post);
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("[post][getPostsByPage] "+ex.getMessage());
        } finally {
            con.close();
            System.out.println("getPostByPage Close con");
        }
        return posts;

    }
	
    @SuppressWarnings("finally")
	public boolean hasReaction(JsonObject obj) throws SQLException {
        Connection con = new Conexao().getConnection();
        System.out.println("hasReaction Open con");
        boolean result = false;
        try {
            String sql = "SELECT * FROM  Reaction_Post WHERE RPT_ID_USER =? AND RPT_ID_POST = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, obj.getLong("id"));
            stmt.setLong(2, this.idFacebook);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("[post][hasReaction] "+ex.getMessage());
        } finally {
            con.close();
            System.out.println("hasReaction Close con");
            return result;
        }
    }
    
    public void saveReactionPost(JsonObject obj) throws SQLException {
        Connection con = new Conexao().getConnection();
        System.out.println("saveReactionPost Open con");
        try {
            String sql = "INSERT INTO Reaction_Post(RPT_ID_USER, RPT_ID_POST, RPT_TYPE) VALUES (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, obj.getLong("id"));
            stmt.setLong(2, this.idFacebook);
            stmt.setString(3, obj.getString("type"));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("[post][saveReactionPost] "+ex.getMessage());
        }finally{
            con.close();
            System.out.println("saveReactionPost Close con");
        }
    }
    
    public static List<Post> loadAllPosts() throws SQLException {
    	List<Post> posts = new ArrayList<>();
        Connection con = new Conexao().getConnection();
        System.out.println("getPosts Open con");
        String sql = "SELECT POS_ID_FACEBOOK,POS_MESSAGE,POS_STORY, POS_CREATED_TIME, POS_PAGE_ID "
                + "FROM POST";

        try {
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setIdFacebook(rs.getLong("POS_ID_FACEBOOK"));
                post.setMessage(rs.getString("POS_MESSAGE"));
                post.setStory(rs.getString("POS_STORY"));
                post.setCreatedTime(rs.getString("POS_CREATED_TIME"));
                MyPage page = new MyPage();
                page.setIdFacebook(rs.getLong("POS_PAGE_ID"));
                post.setPage(page);
                posts.add(post);
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("[post][getPosts] "+ex.getMessage());
        } finally {
            con.close();
            System.out.println("getPosts Close con");
        }
        return posts;
	}

	// Getters and Setters
	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getIdFacebook() {
		return idFacebook;
	}

	public void setIdFacebook(Long idFacebook) {
		this.idFacebook = idFacebook;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public MyPage getPage() {
		return page;
	}

	public void setPage(MyPage page) {
		this.page = page;
	}

	

}
