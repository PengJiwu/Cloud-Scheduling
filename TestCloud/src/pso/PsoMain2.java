package pso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
//import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
//import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
//import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import sheduler.PSO;
import sheduler.PsoParticles;
import util.Processors;
import util.Constants;
import util.VmType;
import net.sourceforge.jswarm_pso.Particle;

public class PsoMain2 {
        static FileOutputStream out;
	static PrintStream ps;
	static double[] mapping;
	static ArrayList<Vm> vmlist;
        static PSO PSOInstance;
        
	public static void main(String[] args) throws IOException {
            
                doPso();
                vmlist = new ArrayList<Vm>();
                initSimulation();
//                printMapping();
		
	}
        private static void doPso(){
            new OpenCloudlet();
            PSOInstance  = new PSO();
            mapping = PSOInstance.run();
        }
	private static void initSimulation() {
		Log.printLine("Starting CloudSim simulation using PSO...");

		try {

			int num_user = 1;
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;
			CloudSim.init(num_user, calendar, trace_flag);
			
                        for (int i = 0; i <Constants.datacenterCount ; i++) {
                            Datacenter datacenter = createDatacenter("Datacenter_"+Integer.toString(i+1) );
                        }

			PSODatacenterBroker broker = createBroker(1);
			int brokerId = broker.getId();

			addVMs(Constants.vmCount, brokerId, false);
			
                        List<Cloudlet> cloudletList = createCloudLets(brokerId,Constants.cloudletSize);
                        
                        /*
                        // mapping our vmIds to cloudsim vmIds
                        HashSet<Integer> vmIds = new HashSet<>();
                        HashMap<Integer, Integer> hm = new HashMap<>();
                        for (Vm vm : vmlist) {
                            if (!vmIds.contains(vm.getId()))
                                vmIds.add(vm.getId());
                        }
                        Iterator<Integer> it = vmIds.iterator();
                        for (int i = 0; i < mapping.length; i++) {
                            if (hm.containsKey((int) mapping[i])) continue;
                            hm.put((int) mapping[i], it.next());
                        }
                        for (int i = 0; i < mapping.length; i++)
                            mapping[i] = hm.containsKey((int) mapping[i]) ? hm.get((int) mapping[i]) : mapping[i];
                        */
			broker.submitVmList(vmlist);
                        broker.setMapping(mapping); 
                        
                        broker.submitCloudletList(cloudletList);


                        // Fifth step: Starts the simulation
                        CloudSim.startSimulation();

                        List<Cloudlet> newList = broker.getCloudletReceivedList();

                        CloudSim.stopSimulation();

                        printCloudletList(newList);

			Log.printLine("CloudSimExample finished!");
                       

		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	private static List<Host> createHost(int numberHosts){
		List<Host> hostList = new ArrayList<Host>();
		
		for(int i = 0 ; i < numberHosts ; i++){

			List<Pe> peList = new ArrayList<Pe>();

			int mips = Processors.Intel.Core_i7_Extreme_Edition_3960X.mips*10;
			int cores = Processors.Intel.Core_i7_Extreme_Edition_3960X.cores;

			for (int j = 0; j < cores; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(mips	/ cores)));
			}

			int host_ID = 1+i;
			int host_ram = 16384; //16 GB
			long host_storage = 4194304; // 4 TB
			int host_bw = 15360; // 15 GB ... Amazon EC2

			hostList.add(new Host(host_ID, new RamProvisionerSimple(host_ram),
					new BwProvisionerSimple(host_bw), host_storage,
					//peList, new VmSchedulerSpaceShared(peList)));
					peList, new VmSchedulerTimeShared(peList)));
					//peList, new VmSchedulerTimeSharedOverSubscription(peList)));
		}
				
		return hostList;
	}

	private static Datacenter createDatacenter(String name) {

		List<Host> hostList = new ArrayList<Host>();
		
		hostList = createHost(5);
		
		String arch = "x86";
		String os = "Linux";
		String vmm = "Xen";
		double time_zone = 10.0;
		double cost = 3.0;
		double costPerMem = 0.05;
		double costPerStorage = 0.001;
		double costPerBw = 0.0;
		
		LinkedList<Storage> storageList = new LinkedList<Storage>();

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		Datacenter datacenter = null;
		
		try {
			datacenter = new Datacenter(name, characteristics, 
								new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	private static PSODatacenterBroker createBroker(int id) {
		PSODatacenterBroker broker = null;
		try {
			broker = new PSODatacenterBroker("Broker" + id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	private static void addVMs(int VMNr, int brokerId, boolean timeSharedScheduling) {

		int mips;
		//int mips = Processors.AMD.Athlon_FX_57.mips;
		long size;
		int ram;
		long bw = 1000;
		int pesNumber = 1;
		String vmm = "Xen";

		for (int i = 0; i < VMNr; i++) {
                        if(i==5)
                            i=0;
                        mips = VmType.type[i][Constants.mips];
                        size = VmType.type[i][Constants.size];
                        ram =  VmType.type[i][Constants.ram];
			Vm vm;
			
			int VM_ID = vmlist.size();
			
			if (timeSharedScheduling) {
				vm = new Vm(VM_ID, brokerId, mips, pesNumber, ram, bw, size, vmm,
						new CloudletSchedulerTimeShared());
			}

			else {
				vm = new Vm(VM_ID, brokerId, mips, pesNumber, ram, bw, size, vmm,
						new CloudletSchedulerSpaceShared());
			}

			vmlist.add(vm);
		}
	}

	private static List<Cloudlet> createCloudLets(int userId, int cloudlets){

		// Creates a container to store Cloudlets
		LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

		//cloudlet parameters
                int []execArray = OpenCloudlet.getTaskList();
		long fileSize = 300;
		long outputSize = 300;
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();
                double[][] execMatrix = OpenCloudlet.getExecMatrix();
                
		Cloudlet[] cloudlet = new Cloudlet[cloudlets];
                int [] map = PsoParticles.map;
                int vmId = 0;
                int k =0 , i = 0;
		while( i < cloudlets) {
                    /*
                    int vmId = (int) (mapping[i]);
                    long length = (long) (1e5 * (execMatrix[i][vmId]));
                    cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                    cloudlet[i].setUserId(userId);
                    list.add(cloudlet[i]);
                            */
                    if(map[k]==-1){
                        vmId++;
                        k++;
                        continue;
                    }
                    long length = (long) (Constants.mulFactor*execArray[map[k]]);
                    cloudlet[map[k]] = new Cloudlet(i, length, pesNumber, outputSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                    cloudlet[map[k]].setUserId(userId);
                    list.add(cloudlet[map[k]]);
                    i++;
                    k++;
                }
		return list;
	}

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" +
                indent + "Data center ID" +
                indent + "VM ID" +
                indent + indent + "Time" +
                indent + "Start Time" +
                indent + "Finish Time");

        double mxFinishTime = 0;
        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumIntegerDigits(2);
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + dft.format(cloudlet.getCloudletId()) + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                Log.printLine(indent + indent + dft.format(cloudlet.getResourceId()) +
                        indent + indent + indent + dft.format(cloudlet.getVmId()) +
                        indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) +
                        indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
            mxFinishTime = Math.max(mxFinishTime, cloudlet.getFinishTime());
        }
        Log.printLine(mxFinishTime);
        
        PSOInstance.printBestFitness();
    }
}
