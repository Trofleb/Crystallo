import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

class escherServer {
   public static void main(String[] args) {
     ServerSocket serverSocket = null;
     boolean listening = true;
     int port = 6886;
     Socket socket = null;
     int i;
     int num=0;
     int maxnum=20;
     String dir="/src/httpd/htdocs/escher/savedir";
     boolean verbose = false;

     for(i=0; i< args.length; i++) {
       if (args[i].equals("-max"))
         maxnum = Integer.parseInt(args[++i]);
       else if (args[i].equals("-port"))
         port = Integer.parseInt(args[++i]);
       else if (args[i].equals("-start"))
         num = Integer.parseInt(args[++i]);
       else if (args[i].equals("-dir"))
         dir = args[++i];
       else if (args[i].equals("-verbose"))
         verbose = true;
       else {
         System.out.println("error:  option " + args[i] + " unknown.");
         System.exit(1);
       }
     }
     
     try {
       serverSocket = new ServerSocket(port);
     } catch (IOException e) {
       System.err.println("Could not listen on port: " + port + ", " +
                          e.getMessage());
       System.exit(1);
     }
     
     if (verbose)
       System.out.println("escherServer starting on port " + port);
     while (listening) {
       // wait for a connection
       Socket clientSocket = null;
       try {
         socket = serverSocket.accept();
       } catch (IOException e) {
         continue;
       }

       if (verbose)
         System.out.println("got connection:\n  " + socket);

       // open the sockets and create the streams
       DataInputStream is = null;
       DataOutputStream os = null;
       try {
         is = new DataInputStream(socket.getInputStream());
         os = new DataOutputStream(socket.getOutputStream());
         // parse the data.
         // first, two ints dictating the size of the incoming picture.
         int x = is.readInt();
         int y = is.readInt();
         // x offset in second row
         int xoff = is.readInt();
         int len = is.readInt();
         String name="";
         for(i=1; i <= len; i++)
           name = name + is.readChar();
         
         int scale;
         int maxsize = 512;
         for(scale = 1; scale*y < maxsize && scale*x < maxsize && 
               (scale*xoff)%x != 0; scale++);
         int xfinal = x*scale;
         int yfinal = y*scale;
         int pixels[] = new int[x*y];
         
         
         if (verbose)
           System.out.println("Getting image: x=" + x + ", y=" + y + ", xoff=" +
                              xoff + "\n  xfinal=" + xfinal + ", yfinal=" + 
                              yfinal + ", scale=" + scale);
         boolean save = false;
         for(i=0; i < x; i++) {
           for(int j=0; j < y; j++) {
             pixels[i+j*x] = is.readInt();
             if (i>0 && j>0 && pixels[i+j*x] != pixels[i+j*x-1])
               save=true;
           }
         }

         int finalpixels[] = new int[xfinal*yfinal];
         for(i=-x; i < xfinal+x; i++) {
           for(int j=0; j < yfinal; j++) {
             int newx = i+(xoff*(j/y))%x;
             if (newx >= 0 && newx < xfinal)
               finalpixels[newx+j*xfinal] = pixels[mod(i,x)+(j%y)*x];
           }
         }
         

         // save the index file as a ppm and convert it to gif.
         String fileName = dir + "/index_" + num;
         Runtime rt = Runtime.getRuntime();
         
         if (save) {
           
           if (verbose)
             System.out.println("saving to " + fileName + ".ppm");

           FileOutputStream fo = 
             new FileOutputStream(fileName + ".ppm");
           new PpmEncoder(new MemoryImageSource(x,y,pixels,0,x), fo).encode();
         
           if (verbose)
             System.out.println("converting it to a .gif");
         
           Process p = 
             rt.exec("/sft/osf1/x11/bin/convert " + fileName + ".ppm " + 
                     fileName + ".gif");
           try {
             p.waitFor();
           } catch (java.lang.InterruptedException e) {
             System.out.println(e.getMessage());
           }

           new File(fileName + ".ppm").delete();

           fo = new FileOutputStream(fileName + ".data");
           DataOutputStream dos = new DataOutputStream(fo);
           dos.writeBytes("x " + x + "\n");
           dos.writeBytes("y " + y + "\n");
           dos.writeBytes("xoff " + xoff + "\n");
           dos.writeBytes("type " + name + "\n");
           dos.close();
           fo.close();

         // save the image as a fully patterned file.
           fileName = dir + "/save_" + num;

           if (verbose)
             System.out.println("saving to " + fileName + ".ppm");

           fo = new FileOutputStream(fileName + ".ppm");
           new PpmEncoder(
             new MemoryImageSource(xfinal,yfinal,finalpixels,0,xfinal), 
             fo).encode();
           fo.close();
           if (verbose)
             System.out.println("converting it to a .gif");
           p = rt.exec("/sft/osf1/x11/bin/convert " + fileName + ".ppm " + 
                       fileName + ".gif");
           try {
             p.waitFor();
           } catch (java.lang.InterruptedException e) {
             System.out.println(e.getMessage());
           }

           new File(fileName + ".ppm").delete();

           if (verbose)
             System.out.println("done");
           num++;
           if (num >= maxnum)
             num = 0;
           os.writeInt(1);
         } else {
           if (verbose)
             System.out.println("blank image");
           os.writeInt(10);
         }
         is.close();
         os.close();
       } catch (IOException e) {
         e.printStackTrace();
       }

       try {
         socket.close();
       } catch (IOException e) {
       }
     }
   }

   public static int mod(int x, int y) {
     x = x%y;
     return ((x >= 0) ? x : x+y);
   }
}
