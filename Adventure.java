import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
/**
 * A basic adventure game where a player moves from room to room interacting with things in the rooms.
 * The initial world is created from a text file named world.txt.
 */
class Adventure {
	public static void main(String args[]) throws IOException {
		String intro = "Welcome to Zombie Buster.\n" +
                       "You can move around by clicking north, south, east, or west.\n" +
                       "You can see what is in the room you are in by clicking look.\n" +
                       "You can pick things up by clicking 'pickup thing' where thing is what you see in the room.\n"+
                       "You can drop things you are carrying by clicking 'drop thing' where thing names someting you have.\n"+
                       "You can see your status by clicking status and quit by clicking quit.\n"+
                       "You can shoot your gun by clicking 'shoot'.\n"+
                       "You can reload if you have a gun and ammo by clicking 'reload'.\n"+
                       "Your objective is to escape the zombie infested police station by picking up a key and fuel, dropping it off in the helicopter and typing in 'launch'.\n" +
                       "Your gun only has 3 bullets so it is important to pick up ammo.\n"+
                       "You only have 4 health points, and the zombie will attack if its not dealt with.\n";
    JFrame frame = new JFrame("Zombie Buster");
    Container pane = frame.getContentPane();
    pane.setLayout(new FlowLayout());
    JButton quit = new JButton("quit");
    JButton north = new JButton("north");
    JButton east = new JButton("east");
    JButton south = new JButton("south");
    JButton west = new JButton("west");
    JButton shoot = new JButton("shoot");
    JButton pickup = new JButton("pickup");
    JButton look = new JButton("look");
    JButton reload = new JButton("reload");
    JButton drop = new JButton("drop");
    JButton launch = new JButton("launch");
    JButton status = new JButton("status");
    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout(6,5));
    buttons.add(north);
    buttons.add(east);
    buttons.add(south);
    buttons.add(west);
    buttons.add(look);
    buttons.add(pickup);
    buttons.add(drop);
    buttons.add(shoot);
    buttons.add(reload);
    buttons.add(launch);
    buttons.add(quit);
    buttons.add(status);
    pane.add(buttons, BorderLayout.WEST);
    JTextArea output = new JTextArea(20, 60);
  
    output.setText(intro);
    pane.add(output, BorderLayout.CENTER);
    Player player = new Player();
    Zombie zombieA = new Zombie();
    Zombie zombieB = new Zombie();
    Ammo ammo = new Ammo();
    AdventureListener listener = new AdventureListener(player, zombieA, zombieB, ammo, output);
    quit.addActionListener(listener);
    north.addActionListener(listener);
    south.addActionListener(listener);
    east.addActionListener(listener);
    west.addActionListener(listener);
    shoot.addActionListener(listener);
    pickup.addActionListener(listener);
    look.addActionListener(listener);
    reload.addActionListener(listener);
    drop.addActionListener(listener);
    launch.addActionListener(listener);
    status.addActionListener(listener); 
    frame.setSize(950,700);
    frame.setVisible(true);


	Room entryWay;
    Random rand;
    //int ammo;
    if (args.length == 2) {
      // use a seed if provided for testing
      rand = new Random(Integer.parseInt(args[1]));
    }
    else {
      rand = new Random(); // let it be really random
    }
    if (args.length >= 1) {
      entryWay = randomWorld(args[0], rand);
    }
    else {
      entryWay = randomWorld("world.txt", rand);
    }
    
    player.moveTo(entryWay);
    
    
  }
  
  /**
   * Read the input file for the list of rooms and their content, then connect them randomly.
   * @param fileName - the name of the world specification file.
   */
  static Room randomWorld(String fileName, Random rand) throws IOException {
    Scanner fileIn = new Scanner(new File(fileName));
    ArrayList<Room> rooms = new ArrayList<Room>();
    
    // first create the rooms and their content - first room is the entrance room
    Room entrance = new Room(fileIn.nextLine());
    addStuff(entrance, fileIn);
    rooms.add(entrance);
    
    // add more rooms
    while (fileIn.hasNextLine()) {
      String name = fileIn.nextLine();
      if (name.equals("*****")) break; // YUK!
      else {
        Room room = new Room(name);
        addStuff(room, fileIn);
        rooms.add(room);   
      }
    }
    
    // now connect the rooms randomly
    for (Room room : rooms) {
      room.connectNorth(rooms.get(rand.nextInt(rooms.size())));
      room.connectEast(rooms.get(rand.nextInt(rooms.size())));
      room.connectSouth(rooms.get(rand.nextInt(rooms.size())));
      room.connectWest(rooms.get(rand.nextInt(rooms.size())));
    }   
    return entrance;    
  }
  
  /**
   * Assumes there is always a blank line after the last line of stuff being added.
   * @param room - the room to fill.
   * @param in - a Scanner reading from the world specification file, ready to read the next room.
   */
  static void addStuff(Room room, Scanner in) {
    String name = in.nextLine().trim();
    while (name.length() > 0) {
      if(name.equals("gun")){
        room.add(new Gun());
      if(name.equals("ammo")){
        room.add(new Ammo());
      }
      }else{
        room.add(new Thing(name));
      }
      name = in.nextLine().trim();
    }   
  }
  
  /**
   * Player attempts to move into the specificed room.
   * This could be teleporting or to a connected room. There is no check for passageway.
   * If the room is null, the move will fail.
   * @param Player - the player trying to move.
   * @param room - the room to move to - could be null
   */
  static String enter(Player player, Room room) {
    if (player.moveTo(room)) {
      return "You just entered " + player.getLocation() + "\n";
    }
    else {
      return "That way appears to be blocked.";
    }
  }
  
  /**
   * Display the contents of what the player sees in the room s/he is currently in.
   * @param player - the player doing the looking
   */
  static String look(Player player) {
    String stuff = player.getLocation().whatStuff();
    if (!stuff.equals("")) {
      return "You see:\n" + stuff;
    }
    else {
      return "You see an empty room.";
    }
  }
  
  /**
   * Player attempts to pickup the specified object.
   * @param player - player doing the picking up
   * @param - what to pickup
   */
  static String pickup(Player player, String what) {
    if (player.getLocation().contains(what)) {
      Thing thing = player.pickup(what);
      if (thing != null && !thing.name().equals("zombie") ) {
        return "You now have " + thing;
      }
      else if(thing.name().equals("zombie")){
      	return "You can't carry a zombie silly";
      }
      else {
        return "You can't carry that. You may need to drop something first.";
      }
    }
    else {
      return "I don't see a " + what;
    }
  }
  
  /**
   * Player attempts to drop the specified object.
   * @param player - player doing the dropping
   * @param - what to drop
   */
  static String drop(Player player, String what) {
    if (player.drop(what)) {
      return "You dropped " + what;
    }
    else {
      return "You aren't carrying " + what;
    }
  }
}