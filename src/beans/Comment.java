package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.restfb.json.JsonObject;

import conexao.Conexao;

public class Comment {

	// ids do facebook são imutáveis
	private Long idCommentFacebook;
	private Long idRepliedComment;
	private String message;
	private Long fromId;
	private String fromName;
	private String createdTime;
	private Post post;

	private List<Long> likes;

	public Comment() {
		likes = new ArrayList<>();
	}

	public static Comment loadFromJson(JsonObject obj, Post post) {
		Comment comment = new Comment();

		String x = obj.getString("id").split("_")[1];

		JsonObject obj2 = obj.getJsonObject("from");

		comment.setIdCommentFacebook(Long.parseLong(x));
		comment.setMessage(obj.getString("message"));
		comment.setFromId(obj2.getLong("id"));
		comment.setFromName(obj2.getString("name"));
		comment.setCreatedTime(obj.getString("created_time"));
		comment.setPost(post);
		comment.setIdRepliedComment(Long.parseLong(x));
		return comment;
	}

	public static Comment loadFromJson(JsonObject obj, Post post, Comment comment) {
		Comment reply = Comment.loadFromJson(obj, post);
		reply.setIdRepliedComment(comment.getIdCommentFacebook());
		return reply;
	}

	public boolean isExists() throws SQLException {
		Connection con = new Conexao().getConnection();
		System.out.println("Comment isExists Open con");
		boolean result = false;
		try {
			String sql = "SELECT * FROM Comment WHERE COM_ID_FACEBOOK = ?";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, this.getIdCommentFacebook());
			ResultSet rs = stm.executeQuery();
			result = rs.next();
			rs.close();
			stm.close();
		} catch (SQLException ex) {
			System.out.println("[comment][isExists] " + ex.getMessage());
		} finally {
			con.close();
			System.out.println("Comment isExists Close con");
		}
		return result;
	}

	public void saveComment() throws SQLException {
		Connection con = new Conexao().getConnection();
		System.out.println("saveComment Open con");
		try {
			String sql = "INSERT INTO Comment (COM_ID_FACEBOOK, COM_ID_COMM_REPLIED, COM_MESSAGE, COM_FROM_ID, COM_FROM_NAME, COM_CREATED_TIME, COM_POST_ID) "
					+ "VALUES (?,?,?,?,?,?,?)";
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setLong(1, this.idCommentFacebook);
			stm.setLong(2, this.idRepliedComment);
			stm.setString(3, this.message);
			stm.setLong(4, this.fromId);
			stm.setString(5, this.fromName);
			stm.setString(6, this.createdTime);
			stm.setLong(7, this.getPost().getIdFacebook());
			stm.executeUpdate();
			stm.close();
		} catch (SQLException ex) {
			System.out.println("[comment][saveComment] " + ex.getMessage());
		} finally {
			con.close();
			System.out.println("saveComment Close con");
		}
	}

	// Getters and Setters
	public void setLikes(Long id) {
		this.likes.add(id);
	}

	public List<Long> getLikes() {
		return likes;
	}

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

	public Long getIdCommentFacebook() {
		return idCommentFacebook;
	}

	public void setIdCommentFacebook(Long idFacebook) {
		this.idCommentFacebook = idFacebook;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Long getIdRepliedComment() {
		return idRepliedComment;
	}

	private void setIdRepliedComment(Long idRepliedComment) {
		this.idRepliedComment = idRepliedComment;
	}

	public Long getFromId() {
		return fromId;
	}

	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public static List<Comment> loadAllComments(int mode) throws SQLException {
		
		List<Comment> comments = new ArrayList<>();
        Connection con = new Conexao().getConnection();
        System.out.println("getCommentByPost Open con");
        
        String sql = "";
        
        if(mode == 1)
        	sql = "SELECT * FROM Comment WHERE com_id_facebook = com_id_comm_replied";
        else if (mode == 2)
        	sql = "SELECT * FROM Comment WHERE com_id_facebook <> com_id_comm_replied";
        
        try {
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setCreatedTime(rs.getString("COM_CREATED_TIME"));
                comment.setFromId(rs.getLong("COM_FROM_ID"));
                comment.setFromName(rs.getString("COM_FROM_NAME"));
                comment.setIdCommentFacebook(rs.getLong("COM_ID_FACEBOOK"));
                comment.setIdRepliedComment(rs.getLong("COM_ID_COMM_REPLIED"));
                comment.setMessage(rs.getString("COM_MESSAGE"));
                Post post = new Post();
                post.setIdFacebook(rs.getLong("COM_POST_ID"));
                post.setCreatedTime(rs.getString("COM_CREATED_TIME"));
                comment.setPost(post);
                comments.add(comment);
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            System.out.println("[comment][getCommentsByPost] "+ex.getMessage());
        } finally {
            con.close();
            System.out.println("getCommentByPost Close con");
        }
        return comments;
	}
}
