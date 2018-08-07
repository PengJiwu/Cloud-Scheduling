
package util;

/**
 *
 * @author User
 */
public class VmType {
    public class Type1{
        public static final int ram = 1000; // 1GB
        public static final int disk = 4000; // 4GB
        public static final int mips = Constants.mipsSize;
        public static final int cost = 5000; // per hour
    }
    public class Type2{
        public static final int ram = 750; // 1GB
        public static final int disk = 3000; // 4GB
        public static final int mips = Constants.mipsSize/2;
        public static final int cost = 2000;// per hour
    }
    public class Type3{
        public static final int ram = 500; // 1GB
        public static final int disk = 1500; // 4GB
        public static final int mips = Constants.mipsSize/3;
        public static final int cost = 1000; // per hour
    }
    public class Type4{
        public static final int ram = 250; // 1GB
        public static final int disk = 700; // 4GB
        public static final int mips = Constants.mipsSize/6;
        public static final int cost = 250; // per hour
    }
    public class Type5{
        public static final int ram = 100; // 1GB
        public static final int disk = 500; // 4GB
        public static final int mips = Constants.mipsSize/10; 
        public static final int cost = 100;     // per hour
    }
    public final static int[][] type = {{Type1.ram,Type1.disk,Type1.mips,Type1.cost}
                            ,{Type2.ram,Type2.disk,Type2.mips,Type2.cost},
                            {Type3.ram,Type3.disk,Type3.mips,Type3.cost},
                            {Type4.ram,Type4.disk,Type4.mips,Type4.cost},
                            {Type5.ram,Type5.disk,Type5.mips,Type5.cost}
                            };

}
