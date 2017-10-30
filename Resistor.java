/////////////////////////////////////////////////////////////////////////////////////////////////////////
// Circuit Solver                                                                                      //
// By: David Rabago                                                                                    //
// Description:                                                                                        //
// Date: 08 February 2017                                                                              //
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//import static java.lang.Math.sqrt;//only needed if power is given

//Class for Resistor objects
public class Resistor{
   
   //instance variables   
   protected double resistance = -1;
   protected double current = -1;
   protected double voltageDrop = -1;
   protected String complex = "";
   protected Resistor[] related;
   protected double power = -1;

   //constuctor of obj Resistor   
   public Resistor(double a, double b, double c){
      resistance=a;
      current=b;
      voltageDrop=c;
      //power=d;
      related = new Resistor[2];
   }
   //constructor of an equivalant resistor
   public Resistor(String c, Resistor[] g){
      int length = g.length;
      double r=0;
      complex=c;
      power=-1;
      related = new Resistor[length];
      
      for(int n=0;n<length;n++){
         related[n]=g[n];
      }
      if(c.equals("s")){//series
         double i=-1;
         for(int n=0;n<length;n++){
            r+=g[n].getR();
            if(g[n].getI()>0){ i=g[n].getI();}
         }
         resistance=r;
         current=i;
         voltageDrop=-1;
      }
      if(c.equals("p")){//parallel
         double v=-1;
         r = g[0].getR();
         for(int n=1;n<length;n++){
            r = 1/(1/r + 1/(g[n].getR()));
            if(g[n].getV()>0){ v=g[n].getV();}
         }
         resistance=r;
         current=-1;
         voltageDrop=v;
      }
   }
 
   //getters   
   public double getR(){ return resistance;}
   public double getI(){ return current;}
   public double getV(){ return voltageDrop;}
   public String isComplex(){return complex;}
   public void relation(){String s=complex;for(int i=0;i<related.length;i++)s+=related[i].all();}//prints the resistors related to this one.
   public Resistor[] getRelated(){ return related;}
   public double getP(){ return power;}
   public String all(){ String s= "\nr: "+resistance+"\ni: "+current+"\nv: "+voltageDrop+"\np: "+power+"\n";return s;}
   public void show(){ String s= "r: "+resistance+"\ni: "+current+"\nv: "+voltageDrop+"\np: "+power+"\n";System.out.print(s);}
   
   //mutators that change and check to see if new calculations can be done
   public void setR(double r){ 
      this.resistance= r;      
      this.check();//update resistor
      if(!complex.equals(""))this.checkRelated();//updates related resistors. 
   }
   public void setI(double i){      
      this.current= i;      
      this.check();//update resistor
      if(!complex.equals(""))this.checkRelated();//updates related resistors. 
   }
   public void setV(double v){       
      this.voltageDrop= v;      
      this.check();//update resistor
      if(!complex.equals(""))this.checkRelated();//updates related resistors. 
   }
   public void setP(double p){       
      this.power= p;      
      this.check();//update resistor
      if(!complex.equals(""))this.checkRelated();//updates related resistors. 
   }
   public void setComplex(String c){ this.complex=c; }
   public void setRelated(Resistor[] fam){ related[0]=fam[0];related[1]=fam[1];this.checkRelated();}
   
   //takea resistor and makes all of its features equal to another resistor (in argument)
   public void override(Resistor a){
      //gets all the data
      double r = a.getR();
      double i = a.getI();
      double v = a.getV();
      String c = a.isComplex();
      Resistor[] fam = a.getRelated();
      double p = a.getP();
      
      //overrides all the data
      this.setR(r);
      this.setI(i);
      this.setV(v);
      this.setComplex(c);
      this.setRelated(fam);
      this.setP(p);
   }
   
   //series
   public Resistor s(Resistor z){
      
      //checks for known current. sets the other if only 1 is known. use this current for eq resistor
      double c = -1;
      if(current>=0){ c = current; z.setI(c);}
      if(z.getI()>=0){ c = z.getI(); this.setI(c);}
      
      //find equivalant resistance
      double eqivalantResistance = resistance + z.getR();
      
      //create an equivalant resistor to simplifly circuit
      Resistor x = new Resistor(eqivalantResistance,c,-1);
      x.check();//update resistor
      x.complex="s";//indicates resistor is complex & combined by series
      x.setRelated(this,z);//establishes relationship between equivalent resistor and the other 2 smaller ones
      
      //updates smaller resistors
      x.bS(this,z);
      return x;
   }
   
   //parallel
   public Resistor p(Resistor z){
      //checks for known current. sets the other if only 1 is known. use this current for eq resistor
      double v = -1;
      if(voltageDrop>=0){ v = voltageDrop; z.setV(v);}
      if(z.getV()>=0){ v = z.getV(); this.setV(v);}
      
      //find equivalant resistance if both are known
      double eqivalantResistance = -1;
      if(resistance>=0 && z.getR()>=0){ eqivalantResistance = 1/((1/resistance) + (1/z.getR()));}
      
      //create an equivalant resistor to simplifly circuit
      Resistor x = new Resistor(eqivalantResistance,-1,v);
      x.check();//updates resistor
      x.complex="p";//indicates resistor is complex & combined by parallel
      x.setRelated(this,z);//establishes relationship between equivalent resistor and the other 2 smaller ones
      
      //updates smaller resistors
      x.bP(this,z);
      return x;
   }
   
   //calaculations
   public void cV(){//calculate voltage assuming current and resistance is known
      double v = current*resistance;
      this.setV(v);
   }
   public void cI(){//calculate current assuming voltage and resistance is known
      double i = voltageDrop/resistance;
      this.setI(i);
   }
   public void cR(){//calculate resistance assuming current and voltage is known
      double r = voltageDrop/current;
      this.setR(r);
   }
   public void cP(){
      power = voltageDrop*current;
   }
   /*
   public void cP(String s){//calculate power assuming both given string values are known
      double p;
      switch(s){
         case("vi"):
         case("iv"):p = voltageDrop*current;if(p<0)this.setP(p);break;
         case("ri"):
         case("ir"):this.cV();break;
         case("vr"):
         case("rv"):this.cI();break;
      }
   }*/
   
   //Going Backwards from equivalant resistance.
   public void bP(Resistor a, Resistor b){//parallel
      double v = -1;
      //finds known voltage between the resistors to set equal
      if(a.getV()>=0) v = a.getV();
      if(b.getV()>=0) v = b.getV();
      if(voltageDrop >=0) v = voltageDrop;
      
      //sets the unknown voltages to found voltage
      if(v>=0&&voltageDrop<0) this.setV(v);
      if(v>=0&&a.getV()<0) a.setV(v);
      if(v>=0&&b.getV()<0) b.setV(v);
      
      //checks related resistors regaurdless.
      a.check();
      b.check();
   }
   public void bS(Resistor a, Resistor b){//series
      //sets smaller resistors to have same current because they're in series 
      //BUT only if the small resistors have unknown currents
      if(current>=0&&a.getI()<0) a.setI(current);
      if(current>=0&&b.getI()<0) b.setI(current);
      
      //checks related regaurdless.
      a.check();
      b.check();
   }
   
   //stores relationship between smaller resistors and equivalent resistor.
   public void setRelated(Resistor a, Resistor b){
      related = new Resistor[2];
      related[0]=a;
      related[1]=b;
   }
   
   //checks to see if smaller resistors can be updated
   public void checkRelated(){
      Resistor a = related[0];
      Resistor b = related[1];
      switch(complex){
         case("p"):this.bP(a,b);break;
         case("s"):this.bS(a,b);break;
      }
   }
   
   public void check(){//checks to see if other values can be calculated
      if(voltageDrop>=0&&resistance>=0&&current<0) this.cI();
      if(current>=0&&resistance>=0&&voltageDrop<0) this.cV();
      if(current>=0&&voltageDrop>=0&&resistance<0) this.cR();
      if(current>=0&&voltageDrop>=0&&power<0) this.cP();
      /*
      if(voltageDrop>=0&&resistance>=0&&power<0) this.cP("vr");
      if(current>=0&&resistance>=0&&power<0) this.cP("ir");
      if(current>=0&&voltageDrop>=0&&power<0) this.cP("vi");
      
      /*Extra features when power is given. 
      (rarely happens so commented out)
      if(voltageDrop>=0&&power>=0&&current<0) this.cI("vpi");
      if(resistance>=0&&power>=0&&current<0) this.cI("rpi");
      if(current>=0&&power>=0&&voltageDrop<0) this.cV("ipv");
      if(resistance>=0&&power>=0&&voltageDrop<0) this.cV("rpv");
      if(current>=0&&power>=0&&resistance<0) this.cR("ipr");
      if(voltageDrop>=0&&power>=0&&resistance<0) this.cR("vpr");
      */
   }
   
   /*
   public void cI(String c){//calculate current assuming power is known (rare)
      double i;
      switch(s){
         case("vpi"):
         case("pvi"):i=power/voltageDrop;break;
         case("rpi"):
         case("pri"):i=Math.sqrt(power/resistance);break;
      }
      if(i>=0) this.setI(i);
   }
   public void cV(String c){//calculate voltage assuming power is known (rare)
      double v;
      switch(s){
         case("ipv"):
         case("piv"):v=power/curret;break;
         case("rpv"):
         case("prv"):r=Math.sqrt(power*resistance);break;
      }
      if(v>=0) this.setV(v);
   }
   public void cR(String c){//calculate resistance assuming power is known (rare)
      double r;
      switch(s){
         case("vpr"):
         case("pvr"):r=voltageDrop*voltageDrop/power;break;
         case("ipr"):
         case("pir"):r=power/(current*current);break;
      }
      if(r>=0) this.setR(r);
   }*/
   
}//end of class