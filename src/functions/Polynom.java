package functions;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Polynom extends Function {
    private ArrayList<Double> fList;
    private ArrayList<Double> xList;
    private double[][] fdList;

    private Function function;
    private int bendCount;
    private int startPoint;
    private int endPoint;

    public Polynom(Function function, int bendCount, int startPoint, int endPoint) {
        this.function = function;
        this.bendCount = bendCount;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        fList = new ArrayList<>();
        xList = new ArrayList<>();
        fdList = new double[bendCount][bendCount];
        fillLists();
    }

    private void fillLists() {
        xList.clear();
        fList.clear();

        double h = Math.abs(endPoint - startPoint) * 1.0 / bendCount;
        for (int i = 0; i < bendCount; i++) {
            xList.add(endPoint - i * h);
            fList.add(function.getValueAt(xList.get(i)));
            if (fList.get(i).isNaN()){
                fList.remove(i);
                fList.add(Double.MIN_VALUE);
            }
            fdList[0][i] = fList.get(i);
        }

        for (int i = 1; i < bendCount; i++) {
            fdList[i][0] = getFiniteDifferences(i, 0);

            for (int k =0;k<i+1;k++){
                for (int j =0;j<i+1;j++){
                    System.out.print(fdList[k][j]+" ");
                }
                System.out.println();
            }
            System.out.println();
            //System.out.println("Current bend: " + i);
        }

        for (int i =0;i<bendCount;i++){
            for (int j =0;j<bendCount;j++){
                System.out.print(fdList[i][j]+" ");
            }
            System.out.println();
        }
    }

    private double getFiniteDifferences(int power, int num) {
        /*BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i <= power; i++) {
            BigDecimal tmp = BigDecimal.ONE;
            if ((power - i) % 2 == 1) {
                tmp = tmp.multiply(BigDecimal.valueOf(-1));
            }
            if (power == 46 && i ==45){
                System.out.println(i);
            }
            tmp = tmp.multiply(getNumberOfCombinations(power, i));
            tmp = tmp.multiply(BigDecimal.valueOf(fList.get(i)));
            sum = sum.add(tmp);
        }

        System.out.println(sum);
        return sum;*/

        if(power == 1){
            fdList[1][num] = fdList[0][num] - fdList[0][num+1];
            return fdList[1][num];
        }
        fdList[power][num] = fdList[power-1][num] - getFiniteDifferences(power-1, num+1);
        return fdList[power][num];
    }

    private BigDecimal getNumberOfCombinations(int n, int k) {
        BigDecimal tmp = factorial(n).divide(factorial(k).multiply(factorial(n - k)));
        return tmp;
    }

    public static BigDecimal factorial(int number) {
        BigDecimal result = BigDecimal.valueOf(1);

        for (long factor = 2; factor <= number; factor++) {
            result = result.multiply(BigDecimal.valueOf(factor));
        }

        return result;
    }

    @Override
    public double getValueAt(double x) {
        double sum = fdList[0][0];
        double tmp = 1;
        double h = Math.abs(endPoint - startPoint) * 1.0 / bendCount;
        double t = (x - endPoint) / h;
        for (int i = 0; i < bendCount-1; i++) {
            tmp *= (t + i)/(i + 1);
            sum += fdList[i+1][0]*tmp;
        }
        return sum;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
        fillLists();
    }

    public int getBendCount() {
        return bendCount;
    }

    public void setBendCount(int bendCount) {
        this.bendCount = bendCount;
        fillLists();
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
        fillLists();
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
        fillLists();
    }

}
