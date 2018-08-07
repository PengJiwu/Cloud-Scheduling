/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Arrays;
import pso.OpenCloudlet;
import util.Constants;

/**
 *
 * @author User
 */
public class Test {
    public static void main(String[] args) {
        new OpenCloudlet();
        double [][] a = OpenCloudlet.getExecMatrix();
        for(int i =0 ;i < Constants.cloudletSize; i++){
            for(int j =0; j< Constants.vmCount; j++){
                System.out.print(i);
                System.out.print(" : ");
                System.out.print(a[i][j]);
                System.out.print(" , ");
            }
            System.out.println("");
        }
    }    
}
