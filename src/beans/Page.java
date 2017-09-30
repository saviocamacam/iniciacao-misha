package beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexao.Conexao;

public class Page {

	private Long idFacebook; // PAG_ID_FACEBOOK
	private String politicalParty; // PAG_POLITICAL_PARTY
	private String state; // PAG_STATE
	private String name; // PAG_NAME
	private List<Post> posts;
	private List<Long> likes;

	public Page() {
		// TODO Auto-generated constructor stub
	}

	public static List<Page> getPages() throws SQLException {
		List<Page> pages = new ArrayList<>();
		Connection con = new Conexao().getConnection();
		System.out.println("getPages Open con");
		String sql = "SELECT PAG_ID_FACEBOOK,PAG_NAME, PAG_POLITICAL_PARTY, PAG_STATE " + "FROM Page";
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				Page page = new Page();
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

}
