import java.util.*;
import java.awt.event.*;
import javax.swing.*;

class AdventureListener implements ActionListener {
  private Player player;
  private Zombie zombieA;
  private Zombie zombieB;
  private Ammo ammo;
  private Random rand = new Random();
  private JTextArea output;
  AdventureListener(Player player, Zombie zombieA, Zombie zombieB, Ammo ammo, JTextArea output) {
      this.player = player;
      this.zombieA = zombieA;
      this.zombieB = zombieB;
      this.ammo = ammo;
      this.output = output;
  }
  
  public void actionPerformed(ActionEvent e) {
    String action = "";
    int chance = rand.nextInt(2) + 1; //Your chance of hitting a zombie is 50%(see "shoot" loop)
    if (e.getActionCommand().equals("quit")) {
        action = "Good bye.";
        System.exit(0);
      }
    
    if(player.getHealth() == 0){ //losing condition
        action = "You died!";
        System.exit(0);
      }
    
    if(e.getActionCommand().equals("launch") && player.getLocation().contains("key") && 
       player.getLocation().contains("fuel")){
          action = "Zombies begin to horde around the helicopter. You quickly ignite the engine, and take off without looking back.\n" +
          "                                                 The End                                                                ";
          System.exit(0);
    }
    
    else if(e.getActionCommand().equals("north")) {
      action = Adventure.enter(player, player.getLocation().north());
      //zombieHp = 1; //This just sets the zombies health back to one after you leave a room
        if(player.getLocation().contains("zombie") || player.getLocation().contains("bloody zombie") && (zombieA.getZombieHp() != 0 || 
            zombieB.getZombieHp() != 0)) {//checks to see if there is a zombie
          action = Adventure.enter(player, player.getLocation()) + "A zombie launches itself at you from the corner of the room";
        }
    }
        
     else if(e.getActionCommand().equals("south")) {
       action = Adventure.enter(player, player.getLocation().south());
       //zombieHp = 1;
        if(player.getLocation().contains("zombie") || player.getLocation().contains("bloody zombie") && (zombieA.getZombieHp() != 0 && zombieB.getZombieHp() != 0)) {
          action =  Adventure.enter(player, player.getLocation()) + "You hear some growling in the dark room";
        }
      }
     
     else if (e.getActionCommand().equals("east")) {
       action = Adventure.enter(player, player.getLocation().east());
       //zombieHp = 1;
        if(player.getLocation().contains("zombie") || player.getLocation().contains("bloody zombie") && (zombieA.getZombieHp() != 0 && zombieB.getZombieHp() != 0)) {
          action =  Adventure.enter(player, player.getLocation()) + "A zombie launches at you";
        }
      }
     
      else if (e.getActionCommand().equals("west")) {
        action = Adventure.enter(player, player.getLocation().west());
        //zombieHp = 1;
        if(player.getLocation().contains("zombie") || player.getLocation().contains("bloody zombie") && (zombieA.getZombieHp() != 0 && zombieB.getZombieHp() != 0)) {
          action = Adventure.enter(player, player.getLocation()) + "You smell the rotten flesh of a zombie in the room";
        }
      }
      
      else if(e.getActionCommand().equals("shoot")) {
        Thing gn = player.get("gun");//checks if you have gun or not and prints appropriate message
        if(gn == null){
          action = "You don't have a gun...";
        }
        else if(!player.getLocation().contains("zombie") && zombieA.getZombieHp() == 0){ //Makes it so that you can only shoot at zombies
          action = "There's nothing to shoot at... line 82";
        }
        else if(!player.getLocation().contains("bloody zombie") && zombieB.getZombieHp() == 0) {
          action = "There's nothing to shoot at... line 85";
        }
        //ZombieA
        else if(player.getLocation().contains("bloody zombie") && zombieA.getZombieHp() == 1 && chance == 2 && ammo.getBullets() > 0){ //Checks if the rng landed on 2. If yes then you shoot the zombie
          Gun gun = (Gun)gn;
          ammo.decreaseAmmo(ammo);
          zombieA.decreaseHealth(zombieA);
          action = gun.shoot() + "\nRounds: " +ammo.getBullets();
        }else if(player.getLocation().contains("bloody zombie") && zombieA.getZombieHp() == 1 && chance == 1 && ammo.getBullets() > 0){//Checks if rng landed on 1. If yes then you miss
          Gun gun = (Gun)gn;
          ammo.decreaseAmmo(ammo);
          player.reduceHealth(player);
          action = gun.miss() + "\nRounds: " +ammo.getBullets() + "\nThe zombie took a chunk off your shoulder \n" + "Health: " +player.getHealth();
        }//ZombieB
        else if(player.getLocation().contains("zombie") && zombieB.getZombieHp() == 1 && chance == 2 && ammo.getBullets() > 0){ //Checks if the rng landed on 2. If yes then you shoot the zombie
          Gun gun = (Gun)gn;
          ammo.decreaseAmmo(ammo);
          zombieB.decreaseHealth(zombieB);
          action = gun.shoot() + "\nRounds: " +ammo.getBullets();
        }else if(player.getLocation().contains("zombie") && zombieB.getZombieHp() == 1 && chance == 1 && ammo.getBullets() > 0){//Checks if rng landed on 1. If yes then you miss
          Gun gun = (Gun)gn;
          ammo.decreaseAmmo(ammo);
          player.reduceHealth(player);
          action = gun.miss() + "\nRounds: " +ammo.getBullets() + "\nThe zombie took a chunk off your shoulder \n" + "Health: " +player.getHealth();
        }else if(ammo.getBullets() == 0){ //You can't shoot your gun if it's empty
          Gun gun = (Gun)gn;
          action = gun.empty();
        }
      }
      
      else if (e.getActionCommand().equals("look")) {
        if(player.getLocation().contains("bloody zombie") && zombieA.getZombieHp() == 0){
          action = Adventure.look(player) + "\nThe zombie lays motionless on the ground";
        }
        else if(player.getLocation().contains("zombie") && zombieB.getZombieHp() == 0){
          action = Adventure.look(player) + "\nThe zombie lays motionless on the ground";
        }
        else if(player.getLocation().contains("bloody zombie") && zombieA.getZombieHp() > 0 ) {//Checks to see if there is a zombie and makes it so that the zombie attacks if you are looking around the room 
          player.reduceHealth(player);
          action = Adventure.look(player) + "\nThe zombie took a chunk off your shoulder \n" + "Health: " +player.getHealth();
        }
        else if(player.getLocation().contains("zombie") && zombieB.getZombieHp() > 0 ) {//Checks to see if there is a zombie and makes it so that the zombie attacks if you are looking around the room 
          player.reduceHealth(player);
          action = Adventure.look(player) + "\nThe zombie took a chunk off your shoulder \n" + "Health: " +player.getHealth();
        }
        else{
          action = Adventure.look(player);
        }
      }
      
      
      else if(e.getActionCommand().equals("pickup")) {
        String input = JOptionPane.showInputDialog("Type what you want to pickup");
        action = Adventure.pickup(player, input); // MAGIC NUMBER length of pickup plus a space
        if(player.getLocation().contains("bloody zombie") && zombieA.getZombieHp() > 0 ) {//Didn't want players to simply pick up an object and leave without getting harmed
          player.reduceHealth(player);
          action = "The zombie took a chunk off your shoulder\n" + "Health: " +player.getHealth();
        }
        if(player.getLocation().contains("zombie") && zombieB.getZombieHp() > 0 ) {//Didn't want players to simply pick up an object and leave without getting harmed
          player.reduceHealth(player);
          action = "The zombie took a chunk off your shoulder\n" + "Health: " +player.getHealth();
        }
      }
      
      else if(e.getActionCommand().equals("reload")){
        Thing am = player.get("ammo");
        Thing gn = player.get("gun");
        if(am == null){
          action = "You don't have an ammo box...";
        }
        else if(gn == null){
          action = "You don't have a gun..."; //Does not let you reload if you don't have a gun
        }
        else if(ammo.getBullets()< 3){
          action = "Your gun is full"; //Reloads if your ammo is lower than 3 but makes you drop the ammo after reloading
          ammo.reload(ammo);
          player.drop("ammo");
        }
      }
      
      else if (e.getActionCommand().equals("drop")) {
        String input = JOptionPane.showInputDialog("Type what you want to drop");
        action = Adventure.drop(player, input); // MAGIC NUMBER lenght of drop plus a space 
      }
      
      else if (e.getActionCommand().equals("status")) {
        Thing gn = player.get("gun");
        if(gn != null) {
          action = player.toString() + 
          "\nbullets = " + ammo.getBullets() +
          "\nhealth = " + player.getHealth();
        }
        else{
          action = player.toString() + 
          "\nhealth = " + player.getHealth();
        }
      }
          
      
    
      output.setText(action);
      
  }
  
   
}