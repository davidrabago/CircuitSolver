/////////////////////////////////////////////////////////////////////////////////////////////////////////
// Circuit Solver                                                                                      //
// By: David Rabago                                                                                    //
// Description:                                                                                        //
// Date: 08 February 2017                                                                              //
/////////////////////////////////////////////////////////////////////////////////////////////////////////
import java.util.Scanner;
import java.io.*;

public class CircuitSolver{

   //Precondition: Set up scanner  
   public static void main(String [] args) throws IOException{ 
   /*
      Resistor a = new Resistor(r,i,v); // use (-1) if value is unknown
      System.out.print("Resistor a: \n"+a.all()+"\n\n");
   */  
      Resistor a = new Resistor(-1,-1,-1);
      Resistor b = new Resistor(80,4,-1);
      Resistor c = new Resistor(40,-1,-1);
      Resistor d = new Resistor(60,20,-1);
            
      Resistor bc = b.s(c);
      Resistor ac = a.p(bc);
      Resistor ad = d.s(ac);
      
      System.out.print("Resistor a: \n"+a.all()+"\n\n");
      System.out.print("Resistor b: \n"+b.all()+"\n\n");
      System.out.print("Resistor c: \n"+c.all()+"\n\n");
      System.out.print("Resistor d: \n"+d.all()+"\n\n");
     //*/
      /* Attempt1: Works! but long.
      Resistor a = new Resistor(6,-1,-1);
      Resistor b = new Resistor(9,-1,-1);
      Resistor c = new Resistor(5,-1,-1);
      Resistor d = new Resistor(7.5,-1,-1);
      Resistor e = new Resistor(2.5,-1,-1);
      Resistor f = new Resistor(5,-1,-1);
      Resistor g = new Resistor(7,-1,-1);
      Resistor h = new Resistor(8,-1,-1);
      Resistor i = new Resistor(6,-1,-1);
      Resistor j = new Resistor(2,-1,-1);
      
      Resistor ab = a.s(b);
      Resistor abc = c.p(ab);
      Resistor abcd = d.p(abc);
      Resistor abcde = e.p(abcd);
      Resistor abcdef = f.p(abcde);
      Resistor abcdefg = g.s(abcdef);
      Resistor abcdefgh = h.p(abcdefg);
      Resistor abcdefghi = i.s(abcdefgh);
      Resistor abcdefghij = j.s(abcdefghi);       

      abcdefghij.setV(48);
      abcdefghij.show();
      */
      
      /*Attempt 1.5: works!!!
      Resistor a = new Resistor(10,-1,-1);
      Resistor b = new Resistor(10,-1,-1);
      Resistor c = new Resistor(10,-1,-1);
      
      Resistor[] g1 = {a,b,c};
      Resistor eq = new Resistor("s",g1);
      eq.setV(60);
      eq.show();
      a.show();
      //*/
      
      /*Attempt2: not yet... :(
      Resistor a = new Resistor(6,-1,-1);
      Resistor b = new Resistor(9,-1,-1);
      Resistor c = new Resistor(5,-1,-1);
      Resistor d = new Resistor(7.5,-1,-1);
      Resistor e = new Resistor(2.5,-1,-1);
      Resistor f = new Resistor(5,-1,-1);
      Resistor g = new Resistor(7,-1,-1);
      Resistor h = new Resistor(8,-1,-1);
      Resistor i = new Resistor(6,-1,-1);
      Resistor j = new Resistor(2,-1,-1);
      
      Resistor ab = a.s(b);
      Resistor[] g1 = {ab,c,d,e,f};
      Resistor af = new Resistor("p",g1);     
      Resistor ag = af.s(g);     
      Resistor ah = ag.s(h);
      Resistor[] g2 = {ah,i,j};
      Resistor eq = new Resistor("s",g2);
      eq.setV(48);
      eq.show();
      //*/
   }

}