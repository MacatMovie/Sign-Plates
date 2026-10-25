import signplates.StyleCost;
public class StyleCostTest {
 public static void main(String[] args) {
  int cases=0;
  for(int applied=0;applied<33;applied++) for(int draft=0;draft<33;draft++)
   for(boolean oldGlow:new boolean[]{false,true}) for(boolean newGlow:new boolean[]{false,true}) {
    var cost=StyleCost.between(applied,oldGlow,draft,newGlow);
    if(draft==0 || draft==applied) {if(cost.dyeOrdinal()!=-1)throw new AssertionError("Clearing/unchanged tint must be free");}
    else if(cost.dyeOrdinal()!=draft)throw new AssertionError("Wrong required dye");
    if(cost.glowInk()!=(!oldGlow&&newGlow))throw new AssertionError("Only enabling glow costs ink");
    boolean dye=cost.dyeOrdinal()>=0,ink=cost.glowInk();
    for(int dyeCount:new int[]{0,1,64}) for(int inkCount:new int[]{0,1,64}) {
     boolean allowed=cost.canAfford(dyeCount,inkCount,false);
     if((dye&&dyeCount==0)||(ink&&inkCount==0)) {if(allowed)throw new AssertionError("Partially funded changes must be rejected");}
     else if(!allowed)throw new AssertionError("Funded/free change must be allowed");
     if(!cost.canAfford(dyeCount,inkCount,true))throw new AssertionError("Creative bypass failed");
     cases++;
    }
   }
  System.out.println("PASS: "+cases+" style-cost cases, covering both costs, free clearing, unchanged effects and creative mode.");
 }
}
