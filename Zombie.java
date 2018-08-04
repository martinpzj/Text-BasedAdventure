class Zombie extends Thing{
	private int hp = 1;

  	Zombie(){
    	super("zombie");
  	}

  	int getZombieHp(){
  		return hp;
  	}

  	void decreaseHealth(Zombie temp){
  		if(temp.hp == 0)
  			return;
  		else
  			temp.hp = temp.hp - 1;
  	}

  	boolean canBeCarried() {
    	return false;
  	}

  	void dead() {
    	System.out.println("The zombie lays motionless on the floor");
  	}
}