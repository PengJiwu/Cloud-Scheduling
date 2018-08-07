package pso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import util.Constants;
import util.VmType;

/**
 *
 * @author Shihad
 */
public class OpenCloudlet {

    private static File execFile = new File(Constants.execFilename);
    private static List<Cloudlet> cloudlet ;
    private static int [] execArray = new int[Constants.cloudletSize];
    private static double[][] execMatrix = new double[Constants.cloudletSize][Constants.vmCount];
    
    public static int[] getTaskList() { 
        return execArray;
    }
    public OpenCloudlet(){
        
        if(!execFile.exists()){
            try {
                createFile();
                execArray = readFile();
                execMatrix = createMatrix();
            } catch (Exception ex) {
                Logger.getLogger(OpenCloudlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else{
            try{
                execArray=readFile();
                execMatrix = createMatrix();
            }
            catch(Exception ex){
                System.err.println("Unwanted error happaned....");
            }
        }
        System.out.println("Given cloutlets are ....");
        for(int i =0 ;i < Constants.cloudletSize; i++){
            System.out.println(Double.toString(execArray[i]));
        }
    }
    private static void createFile() throws Exception{
        System.out.println("Creating File Execution matrices....");
        Random r = new Random();
        
        BufferedWriter execBufferedWriter = new BufferedWriter(new FileWriter(Constants.execFilename));
        
        for (int i = 0; i < Constants.cloudletSize; i++) {
            int x = r.nextInt(100)*r.nextInt(15)*r.nextInt(50)*r.nextInt(19);
            while(x<400)
                x = r.nextInt(100)*r.nextInt(15)*r.nextInt(50)*r.nextInt(19);
            String s = String.valueOf(400+x );
            execBufferedWriter.write(s);
            execBufferedWriter.write('\n');
            System.out.println(s);
        }
        execBufferedWriter.close();
    }
    private static int[] readFile() throws Exception{
        System.out.println("Reading the cloudlets");
        BufferedReader exeBufferedReader = new BufferedReader(new FileReader(Constants.execFilename));
        int i = 0, j = 0;
        int [] execArr = new int[Constants.cloudletSize];
        
        do {
            String line = exeBufferedReader.readLine();
            execArr[i] = Integer.parseInt(line);
            ++i;
            j = 0;
        } while (exeBufferedReader.ready());
        
       
        return execArr;
    }
    public static double[][]getExecMatrix(){
        return execMatrix;
    }

    private double[][] createMatrix() {
        double [][]m =new double[Constants.cloudletSize][Constants.vmCount];
         for(int i = 0; i < Constants.cloudletSize; i++){
            for(int j = 0; j < Constants.vmCount; j++){
//                System.out.print(Integer.toString(i)+" , " + Integer.toString(j));
                m[i][j] = (double)Constants.mulFactor*execArray[i]/VmType.type[j][Constants.mips];
            }
//            System.out.println();
        }
         return m;
    }
}
