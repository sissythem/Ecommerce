package util;

import java.util.ArrayList;

/**
 * Created by sissy on 7/5/2017.
 */

public class Pair<T1,T2 extends Double> implements Comparable<Pair> {
    public int getId() {
        return Id;
    }

    int Id;
    int ValueCount;
    public T1 getData() {
        return data;
    }
    public double getValue() {
        return value;
    }

    T1 data;
    Double value;

    public Pair(T1 data, double value) {
        this.data = data;
        this.value = value;
        ValueCount = 0;
    }
    public void appendValue(double val)
    {
        value = value +  val;
        ValueCount ++ ;
    }
    public void doAverage()
    {
        double avg = value.doubleValue() / ValueCount;
        value = avg;
    }
    @Override
    public int compareTo(Pair other) {
        return this.value.compareTo( other.value);
    }

    public static int getIfPresent(ArrayList<Pair<ArrayList<?>,?>> list,int otherId)
    {
        for(int i=0;i< list.size();++i)
        {
            Pair<?,?> p = list.get(i);
            if (otherId == p.getId()) return i;
        }
        return -1;
    }

}