package edu.school21.chase;

import java.util.Random;

public enum ChaseType {
   HUNTER, SECURITY, STALKER, SCOUT, HYBRID;

   public static ChaseType getRandom() {
      ChaseType[] types = ChaseType.values();
      return types[new Random().nextInt(types.length)];
   }
}
