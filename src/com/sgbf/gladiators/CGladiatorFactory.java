package com.sgbf.gladiators;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Creates randomized gladiators from
 * the set of available classes and a
 * list of names
 */
public class CGladiatorFactory {

   /**
    * The pool of available names
    */
   private LinkedList namePool;
   
   /**
    * The pool of available gladiator classes
    */
   private LinkedList gladiatorPool;
   
   /**
    * CONSTRUCTOR
    */
   public CGladiatorFactory()
   {
      // Initialise the pool of available names
      namePool = new LinkedList( 
                  Arrays.asList(
                              "Maximus", "Minimus", "Mediumus",
                              "Julius", "Henryus", "Timothyus",
                              "Bruce Leeus", "Markus", "Shaneus",
                              "Tawonaus", "Jackus", "Bryanus",
                              "Sallyus", "Susanus", "Maryus",
                              "Stannisus", "Nero", "Augustus",
                              "Novemberus", "Germanicus", "Caesar",
                              "Tiberius", "Claudius", "Caligula"
                           )
               );
      
      // Initialise the pool of available gladiator types
      gladiatorPool = new LinkedList(
                        Arrays.asList( 
                                    CMyrmidon.class,
                                    CBarbarian.class,
                                    CSamnite.class
                                 )
                     );
   }
   
   /**
    * Retrieves a new randomly created gladiator
    * @return A new gladiator
    */
   public CGladiator createGladiator()
   {
      // Find a random name and remove it from the pool
      // (i.e. avoid multiple gladiators with the same name)
      int nameIdx = (int)Math.floor( 
                           Math.random()*namePool.size() );
      String name = (String) namePool.remove(nameIdx);
      
      // Find a random gladiator class
      int classIdx = (int)Math.floor( 
                           Math.random()*gladiatorPool.size() );
      Class gladiatorClass = (Class) gladiatorPool.get( classIdx );
      CGladiator gladiator = null;
      try {
         gladiator = (CGladiator)gladiatorClass.
               getConstructor( String.class ).
               newInstance( name );
      }
      catch( Exception e ) {
         // Naughty naughty; we're not handling it!
      }
      
      return gladiator; 
   }
}