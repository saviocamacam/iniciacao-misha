package beans;

public class PostInfo {
	private Long idFacebook;
	private String text;
	private String date;
	
	private float comments;
	private float replies;
	private int shares;
	
	
	private float postLike;
	private float postLove;
	private float postHaha;
	private float postWow;
	private float postSad;
	private float postAngry;
	
	private float meanCommentLike;
	private float meanCommentLove;
	private float meanCommentHaha;
	private float meanCommentWow;
	private float meanCommentSad;
	private float meanCommentAngry;
	
	private float meanReplyLike;
	private float meanReplyLove;
	private float meanReplyHaha;
	private float meanReplyWow;
	private float meanReplySad;
	private float meanReplyAngry;
	
	
	public PostInfo(Long idFacebook) {
		setIdFacebook(idFacebook);
	}


	public Long getIdFacebook() {
		return idFacebook;
	}


	public void setIdFacebook(Long idFacebook2) {
		this.idFacebook = idFacebook2;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getShares() {
		return shares;
	}


	public void setShares(int shares) {
		this.shares = shares;
	}


	public float getReplies() {
		return replies;
	}


	public void setReplies(float commentReply) {
		this.replies = commentReply;
	}


	public float getPostLike() {
		return postLike;
	}


	public void setPostLike(float reactions) {
		this.postLike = reactions;
	}


	public float getPostLove() {
		return postLove;
	}


	public void setPostLove(float reactions) {
		this.postLove = reactions;
	}


	public float getPostHaha() {
		return postHaha;
	}


	public void setPostHaha(float reactions) {
		this.postHaha = reactions;
	}


	public float getPostWow() {
		return postWow;
	}


	public void setPostWow(float reactions) {
		this.postWow = reactions;
	}


	public float getPostSad() {
		return postSad;
	}


	public void setPostSad(float reactions) {
		this.postSad = reactions;
	}


	public float getPostAngry() {
		return postAngry;
	}


	public void setPostAngry(float reactions) {
		this.postAngry = reactions;
	}


	public float getMeanCommentLike() {
		return meanCommentLike;
	}


	public void setMeanCommentLike(float meanCommentLike) {
		this.meanCommentLike = meanCommentLike;
	}


	public float getMeanCommentLove() {
		return meanCommentLove;
	}


	public void setMeanCommentLove(float meanCommentLove) {
		this.meanCommentLove = meanCommentLove;
	}


	public float getMeanCommentHaha() {
		return meanCommentHaha;
	}


	public void setMeanCommentHaha(float meanCommentHaha) {
		this.meanCommentHaha = meanCommentHaha;
	}


	public float getMeanCommentWow() {
		return meanCommentWow;
	}


	public void setMeanCommentWow(float meanCommentWow) {
		this.meanCommentWow = meanCommentWow;
	}


	public float getMeanCommentSad() {
		return meanCommentSad;
	}


	public void setMeanCommentSad(float meanCommentSad) {
		this.meanCommentSad = meanCommentSad;
	}


	public float getMeanCommentAngry() {
		return meanCommentAngry;
	}


	public void setMeanCommentAngry(float meanCommentAngry) {
		this.meanCommentAngry = meanCommentAngry;
	}


	public float getMeanReplyLike() {
		return meanReplyLike;
	}


	public void setMeanReplyLike(float meanReplyLike) {
		this.meanReplyLike = meanReplyLike;
	}


	public float getMeanReplyLove() {
		return meanReplyLove;
	}


	public void setMeanReplyLove(float meanReplyLove) {
		this.meanReplyLove = meanReplyLove;
	}


	public float getMeanReplyHaha() {
		return meanReplyHaha;
	}


	public void setMeanReplyHaha(float meanReplyHaha) {
		this.meanReplyHaha = meanReplyHaha;
	}


	public float getMeanReplyWow() {
		return meanReplyWow;
	}


	public void setMeanReplyWow(float meanReplyWow) {
		this.meanReplyWow = meanReplyWow;
	}


	public float getMeanReplySad() {
		return meanReplySad;
	}


	public void setMeanReplySad(float meanReplySad) {
		this.meanReplySad = meanReplySad;
	}


	public float getMeanReplyAngry() {
		return meanReplyAngry;
	}


	public void setMeanReplyAngry(float meanReplyAngry) {
		this.meanReplyAngry = meanReplyAngry;
	}


	public float getComments() {
		return comments;
	}


	public void setComments(float commentReply) {
		this.comments = commentReply;
	}

}
