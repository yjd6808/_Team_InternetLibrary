//작성자 : 윤정도

package bean;

public class BookBean implements PageData {
	int u_id;
	String name;
	String writerName;
	String publisherName;
	String code;
	int ageLimit;
	int borrowPoint;
	int buyPoint;
	String dataFileName;
	String imageFileName;
	float score;
	int scoreGiverCount;
	
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWriterName() {
		return writerName;
	}
	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(int ageLimit) {
		this.ageLimit = ageLimit;
	}
	public int getBorrowPoint() {
		return borrowPoint;
	}
	public void setBorrowPoint(int borrowPoint) {
		this.borrowPoint = borrowPoint;
	}
	public int getBuyPoint() {
		return buyPoint;
	}
	public void setBuyPoint(int buyPoint) {
		this.buyPoint = buyPoint;
	}
	
	public String getDataFileName() {
		return dataFileName;
	}
	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public int getScoreGiverCount() {
		return scoreGiverCount;
	}
	
	public void setScoreGiverCount(int scoreGiverCount) {
		this.scoreGiverCount = scoreGiverCount;
	}
	
	public String getAverageScore() {
		if (scoreGiverCount == 0) {
			return "0.0";
		}
		
		return String.format("%.1f", score / scoreGiverCount); 
	}
}
