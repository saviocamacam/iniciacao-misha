package beans;

import com.restfb.json.JsonObject;

public class Reaction {
	
	private Long idUser;
	private Long idObject;
	private String type;

	public Reaction() {
		
	}
	
	public static Reaction loadFromJson(JsonObject obj2) {
		Reaction reaction = new Reaction();
		reaction.setIdUser(Long.parseLong(obj2.getString("id")));
		reaction.setType(obj2.getString("type"));
		return reaction;
	}	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getIdObject() {
		return idObject;
	}

	public void setIdObject(Long idObject) {
		this.idObject = idObject;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

}
