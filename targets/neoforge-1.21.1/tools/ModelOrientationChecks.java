package signplates.client;
/** Tests the exact rotation used when assembling cached chunk quads. */
public class ModelOrientationChecks {
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 public static void main(String[] args) {
  for(int face=0;face<3;face++)for(int facing=0;facing<4;facing++) {
   int yaw=(facing+(face==2?2:0))%4;
   float[] n=PlateBakedModel.rotate(0,1,0,face,yaw);
   float[][] expected={{0,1,0},{0,0,-1},{1,0,0},{0,0,1},{-1,0,0},{0,-1,0}};
   float[] e=expected[face==0?0:face==2?5:facing+1];
   for(int axis=0;axis<3;axis++)check(n[axis]==e[axis],"front normal");
   for(float x:new float[]{1/16f,15/16f})for(float y:new float[]{0,1/16f})for(float z:new float[]{1/16f,15/16f}) {
    var p=PlateBakedModel.rotate(x-.5f,y-.5f,z-.5f,face,yaw);for(int axis=0;axis<3;axis++)p[axis]+=.5f;
    int thinAxis=face==1?(facing%2==0?2:0):1;
    boolean upper=face==2||(face==1&&(facing==0||facing==3));
    check(p[thinAxis]>=(upper?15/16f:0)&&p[thinAxis]<=(upper?1:1/16f),"geometry matches collision shape");
    for(float coordinate:p)check(coordinate>=0&&coordinate<=1,"bounds");
   }
  }
  System.out.println("PASS: geometry/normals match collision shapes for all 12 floor/wall/ceiling orientations.");
 }
}
