package App.Characters.Attributes;

public abstract class Behaviour implements IBehaviour {

	protected float weight;

	public Behaviour(float weight) {
		this.weight = weight;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public float getWeight() {
		return weight;
	}
}
