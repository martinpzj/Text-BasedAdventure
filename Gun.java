class Gun extends Thing{
  Gun() {
    super("gun");
  }
  String shoot(){
    return "You blasted a bullet through the zombie's head, and it falls to the ground";
  }
  String miss(){
    return "Your hands were too shaky and missed";
  }
  String empty(){
    return "Your gun is empty";
  }
}