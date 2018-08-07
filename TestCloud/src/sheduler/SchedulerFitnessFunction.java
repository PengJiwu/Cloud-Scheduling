/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sheduler;

import java.util.Arrays;
import net.sourceforge.jswarm_pso.FitnessFunction;
import pso.OpenCloudlet;
import util.Constants;
import util.VmType;


class SchedulerFitnessFunction extends FitnessFunction{

    private static double execMatrix[][] = OpenCloudlet.getExecMatrix();
    private static double [] executionTimePerVm = new double[Constants.vmCount];
    public SchedulerFitnessFunction() {
        super(false);
        
    }
    
    @Override
    public double evaluate(double[] position) {
        double r =0.5;
        return (r*calcMakespan(position)+(1-r)*calcCostPerTime(position) );
//        return calcMakespan(position);
    }
    public double calcMakespan(double[] position) {
        /*double makespan = 0;
        double[] vmWorkingTime = new double[Constants.vmCount];

        for (int i = 0; i < Constants.cloudletSize; i++) {
            int vmId = (int) position[i];
            if(vmWorkingTime[vmId] != 0) --vmWorkingTime[vmId];
            vmWorkingTime[vmId] += execMatrix[i][vmId];
            makespan = Math.max(makespan, vmWorkingTime[vmId]);
        }
        return makespan;
        */
        clearexecutionTimePerVm();
         for(int i = 0; i < Constants.cloudletSize ; i++){
            int vmId = (int) position[i];
            executionTimePerVm[vmId] +=execMatrix[i][vmId];
             /*System.out.println("i = "+Integer.toString(i)+"\nvmid = "+
                     Integer.toString(vmId)+"\nexecutionTimePerVm = "+
                     Double.toString(executionTimePerVm[vmId])+"\nexecMatrix = "+
                     Double.toString(execMatrix[i][vmId]));*/
        }
        return Arrays.stream(executionTimePerVm).max().getAsDouble();
    }
    public double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.cloudletSize; i++) {
            int vmId = (int) position[i];
            totalCost += execMatrix[i][vmId];
        }
        return totalCost;
    }
    public double calcCostPerTime(double[] position){
        double totalCost = 0;
        for (int i = 0; i < Constants.cloudletSize; i++) {
            int vmId = (int) position[i];
            totalCost += execMatrix[i][vmId]*VmType.type[vmId][Constants.cost];
        }
        return totalCost/10000.0;
    }
    public void clearexecutionTimePerVm(){
        for(int i = 0; i < executionTimePerVm.length; i++)
            executionTimePerVm[i] = 0;
    }


}
