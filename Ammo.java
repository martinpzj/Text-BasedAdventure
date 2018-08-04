class Ammo extends Thing{
	private int bullets = 3;

	Ammo() {
		super("ammo");
	}

	int getBullets() {
		return bullets;
	}

	void decreaseAmmo(Ammo temp){
		temp.bullets = temp.bullets - 1;
	}

	void reload(Ammo temp){
		temp.bullets = 3;
	}

}