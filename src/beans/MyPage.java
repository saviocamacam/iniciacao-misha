package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.restfb.json.JsonObject;

import conexao.Conexao;

public class MyPage {

	private Long idFacebook; // PAG_ID_FACEBOOK
	private String politicalParty; // PAG_POLITICAL_PARTY
	private String state; // PAG_STATE
	private String name; // PAG_NAME
	private List<Post> posts;
	private List<Long> likes;

	public MyPage() {
	}

	public static List<MyPage> getPages() throws SQLException {
		List<MyPage> pages = new ArrayList<>();
		Connection con = new Conexao().getConnection();
		System.out.println("getPages Open con");
		String sql = "SELECT PAG_ID_FACEBOOK,PAG_NAME, PAG_POLITICAL_PARTY, PAG_STATE " + "FROM Page";
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				MyPage page = new MyPage();
				page.setIdFacebook(rs.getLong("PAG_ID_FACEBOOK"));
				page.setName(rs.getString("PAG_NAME"));
				page.setState(rs.getString("PAG_STATE"));
				page.setPoliticalParty(rs.getString("PAG_POLITICAL_PARTY"));
				pages.add(page);
			}
			rs.close();
			stm.close();
		} catch (SQLException ex) {
			System.out.println("[page][getPages] " + ex.getMessage());
		} finally {
			con.close();
			System.out.println("getPages Close con");
		}
		return pages;
	}

	public String getPoliticalParty() {
		return politicalParty;
	}

	public String getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPoliticalParty(String politicalParty) {
		this.politicalParty = politicalParty;
	}

	public Long getIdFacebook() {
		return this.idFacebook;
	}

	public void setIdFacebook(Long idFacebook) {
		this.idFacebook = idFacebook;
	}

	public static MyPage loadFromJson(JsonObject obj) {
		MyPage page = new MyPage();
		page.setIdFacebook(Long.parseLong(obj.getString("id")));
		page.setName(obj.getString("name"));
		return page;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Long> getLikes() {
		return likes;
	}

	public void setLikes(List<Long> likes) {
		this.likes = likes;
	}

}
