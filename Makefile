#------------------------------------------------------------------------------
#Makefile for Text-BasedAdventure
#------------------------------------------------------------------------------
JAVASRC = Adventure.java AdventureListener.java Room.java Player.java Thing.java Ammo.java Fuel.java Gun.java Key.java Zombie.java
SOURCES = README Makefile $(JAVASRC)
MAINCLASS = Adventure
CLASSES = Adventure.class AdventureListener.class Room.class Player.class Thing.class Ammo.class Fuel.class Gun.class Key.class Zombie.class
JARFILE = ZombieBuster.jar
all: $(JARFILE)
$(JARFILE): $(CLASSES)
	echo Main-class: $(MAINCLASS) > Manifest
	jar cvfm $(JARFILE) Manifest $(CLASSES)
	rm Manifest
$(CLASSES): $(JAVASRC)
	javac -Xlint $(JAVASRC)
clean:
	rm $(CLASSES) $(JARFILE)