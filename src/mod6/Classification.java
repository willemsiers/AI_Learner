package mod6;

public class Classification {

	public double prob;
	public String[] words;
	public Model model;

	public Classification(String name, double prob, Model writer, String[] words) {
		this.prob = prob;
		this.model = writer;
		this.words = words;
	}
	
	@Override
	public String toString() {
		return String.format("%s\t(%f)", this.model.name, this.prob);
	}

}
